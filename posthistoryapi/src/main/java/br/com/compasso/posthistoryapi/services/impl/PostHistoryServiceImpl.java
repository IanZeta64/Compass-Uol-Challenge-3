package br.com.compasso.posthistoryapi.services.impl;

import br.com.compasso.posthistoryapi.client.PostClient;
import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.entity.Comment;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.CommentNotFoundException;
import br.com.compasso.posthistoryapi.exceptions.DuplicatePostException;
import br.com.compasso.posthistoryapi.exceptions.HistoryNotFoundException;
import br.com.compasso.posthistoryapi.exceptions.PostNotFoundException;
import br.com.compasso.posthistoryapi.manager.PostManager;
import br.com.compasso.posthistoryapi.manager.states.FailedState;
import br.com.compasso.posthistoryapi.message.publisher.MessageProducer;
import br.com.compasso.posthistoryapi.repositories.CommentRepository;
import br.com.compasso.posthistoryapi.repositories.HistoryRepository;
import br.com.compasso.posthistoryapi.repositories.PostRepostory;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.compasso.posthistoryapi.constants.GlobalConstants.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostHistoryServiceImpl implements PostHistoryService {

    private final PostClient client;
    private final HistoryRepository historyRepository;
    private final PostRepostory postRepostory;
    private final CommentRepository commentRepository;
    private final MessageProducer mqProducer;

    @Override
    public void process(Long postId) {
        log.info("PROCESS - checking for exists post -{}", postId);
        if (postRepostory.existsById(postId)) {
            throw new DuplicatePostException("");
        }
        createPostHistoryChain(postId);
    }


    @Override
    public void disable(Long postId) {
        log.info("DISABLE - checking for exists post -{}", postId);
        if (!postRepostory.existsById(postId)) {
            throw new HistoryNotFoundException("");
        }
        disablePostHistoryChain(postId);
    }

    @Override
    public void reprocess(Long postId) {
        log.info("REPROCESS - checking for exists post -{}", postId);
        if (!postRepostory.existsById(postId)) {
            throw new HistoryNotFoundException("");
        }
        updatePostHistoryChain(postId);
    }

    @Override
    public List<PostDtoResponse> findAll() {
        log.info("QUERY - Getting all posts");
        return postRepostory.findAll().parallelStream().map(PostDtoResponse::new).toList();
    }

    private void createPostHistoryChain(Long postId) {
        log.info("PROCESS - Creating new history post -{}", postId);
        PostManager postManager = new PostManager(postId);
        createHistoryStatus(postManager);
    }

    private void updatePostHistoryChain(Long postId) {
        log.info("PROCESS - Updating post -{}", postId);
        postRepostory.findById(postId).ifPresentOrElse(post ->
                        setUpdatingStatus(new PostManager(post, REPROCESS_QUEUE)),
                () -> {
                    throw new PostNotFoundException("");
                });
    }

    private void disablePostHistoryChain(Long postId) {
        log.info("PROCESS - Disabling post -{}", postId);
        postRepostory.findById(postId).ifPresentOrElse(post ->
                        setDisabledStatus(new PostManager(post, DISABLE_QUEUE)),
                () -> {
                    throw new PostNotFoundException("");
                });
    }

    private void createHistoryStatus(PostManager postManager) {
        postManager.handleState();
        postRepostory.save(new Post(postManager.getPostId(), postManager.getHistories()));
        setPostFindStatus(postManager);
        log.info("CREATED - Created new history post -{}", postManager.getPostId());
    }

    private void setPostFindStatus(PostManager postManager) {
        log.info("POST_FIND - Finding post -{}", postManager.getPostId());
        try {
            PostDto postFound = client.findPostById(postManager.getPostId());
            History history = postManager.handleState();
            historyRepository.save(history);
            setPostOkStatus(postManager, postFound);
            mqProducer.sendMessage(POST_OK_QUEUE, postManager.getPostId());
        } catch (Exception e) {
            History historyFailed = postManager.handleFailed();
            historyRepository.save(historyFailed);
            mqProducer.sendMessage(FAILED_QUEUE, postManager.getPostId());
        }


    }

    private void setPostOkStatus(PostManager postManager, PostDto postFound) {
        log.info("POST_OK - Post founded post -{}", postManager.getPostId());
        postManager.handleState();
        postRepostory.save(new Post(postFound, postManager.getHistories()));

    }

    @JmsListener(destination = POST_OK_QUEUE)
    private void setCommentFindStatus(Long postId) {
        log.info("COMMENT_FIND - Finding comments for post -{}", postId);
        PostManager postManager = new PostManager(new Post(postId), POST_OK_QUEUE);
        try {
            List<CommentDto> commentFound = client.findCommentByPostId(postManager.getPostId());
            History history = postManager.handleState();
            historyRepository.save(history);
            setCommentOkStatus(postManager, commentFound);
        } catch (Exception e) {
           History historyFailed = postManager.handleFailed();
            historyRepository.save(historyFailed);
            setFailedStatus(postManager);
            throw new CommentNotFoundException("");
        }

    }

    private void setCommentOkStatus(PostManager postManager, List<CommentDto> commentFound) {
        log.info("COMMENT_OK - Comments founded for post -{}", postManager.getPostId());
        History history = postManager.handleState();
        historyRepository.save(history);
        commentRepository.saveAll(commentFound.stream().
                map(commentDto -> new Comment(commentDto, postManager.getPostId())).toList());
        setEnabledStatus(postManager);
    }

    private void setEnabledStatus(PostManager postManager) {
        log.info("ENABLED - Enabled post -{}", postManager.getPostId());
        History history = postManager.handleState();
        historyRepository.save(history);
    }
    @JmsListener(destination = FAILED_QUEUE)
    private void failedStatusListener(Long postId){
        PostManager postManager = new PostManager(new Post(postId), FAILED_QUEUE);
        setFailedStatus(postManager);
    }

    private void setFailedStatus(PostManager postManager) {
        log.info("FAILED - Failed to found post/comments -{}", postManager.getPostId());
        History history = postManager.handleState();
        historyRepository.save(history);
        setDisabledStatus(postManager);
    }

    private void setDisabledStatus(PostManager postManager) {
        log.info("DISABLED - Disabled post -{}", postManager.getPostId());
        History history = postManager.handleState();
        historyRepository.save(history);
    }

    private void setUpdatingStatus(PostManager postManager) {
        log.info("UPDATING - Updating post -{}", postManager.getPostId());
        History history = postManager.handleState();
        historyRepository.save(history);
        setPostFindStatus(postManager);
    }
}
