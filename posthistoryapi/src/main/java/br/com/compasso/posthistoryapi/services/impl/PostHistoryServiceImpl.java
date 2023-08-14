package br.com.compasso.posthistoryapi.services.impl;

import br.com.compasso.posthistoryapi.client.PostClient;
import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.entity.Comment;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.exceptions.exceptionclass.*;
import br.com.compasso.posthistoryapi.statemanager.PostStateManager;
import br.com.compasso.posthistoryapi.message.publisher.MessageProducer;
import br.com.compasso.posthistoryapi.repositories.CommentRepository;
import br.com.compasso.posthistoryapi.repositories.HistoryRepository;
import br.com.compasso.posthistoryapi.repositories.PostRepostory;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
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
    @Async
    public void process(Long postId) {
        log.info("PROCESS - checking for exists post -{}", postId);
        if (postRepostory.existsById(postId)) {
            throw new DuplicatePostException("");
        }
        createPostHistoryChain(postId);
    }


    @Override
    @Async
    public void disable(Long postId) {
        log.info("DISABLE - checking for exists post -{}", postId);
        if (!postRepostory.existsById(postId)) {
            throw new HistoryNotFoundException("");
        }
        disablePostHistoryChain(postId);
    }

    @Override
    @Async
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
    @Async
    public void createPostHistoryChain(Long postId) {
        log.info("PROCESS - Creating new history post -{}", postId);
        PostStateManager postStateManager = new PostStateManager(postId);
        createHistoryStatus(postStateManager);
    }
    @Async
    public void updatePostHistoryChain(Long postId) {
        log.info("PROCESS - Updating post -{}", postId);
        postRepostory.findById(postId).ifPresentOrElse(post ->
                        setUpdatingStatus(new PostStateManager(post, REPROCESS_QUEUE)),
                () -> {
                    throw new PostNotFoundException("");
                });
    }
    @Async
    public void disablePostHistoryChain(Long postId) {
        log.info("PROCESS - Disabling post -{}", postId);
        postRepostory.findById(postId).ifPresentOrElse(post ->
                        setDisabledStatus(new PostStateManager(post, DISABLE_QUEUE)),
                () -> {
                    throw new PostNotFoundException("");
                });
    }
    @Async
    public void createHistoryStatus(PostStateManager postStateManager) {
        postStateManager.handleState();
        postRepostory.save(new Post(postStateManager.getPostId(), postStateManager.getHistories()));
        setPostFindStatus(postStateManager);
        log.info("CREATED - Created new history post -{}", postStateManager.getPostId());
    }
    @Async
    public void setPostFindStatus(PostStateManager postStateManager) {
        log.info("POST_FIND - Finding post -{}", postStateManager.getPostId());
        try {
            PostDto postFound = client.findPostById(postStateManager.getPostId());
            History history = postStateManager.handleState();
            historyRepository.save(history);
            setPostOkStatus(postStateManager, postFound);
            mqProducer.sendMessage(POST_OK_QUEUE, postStateManager.getPostId());
        } catch (Exception e) {
            History historyFailed = postStateManager.handleFailed();
            historyRepository.save(historyFailed);
            mqProducer.sendObjectMessage(FAILED_QUEUE, historyFailed);
        }
    }
    @Async
    public void setPostOkStatus(PostStateManager postStateManager, PostDto postFound) {
        log.info("POST_OK - Post founded post -{}", postStateManager.getPostId());
        postStateManager.handleState();
        postRepostory.save(new Post(postFound, postStateManager.getHistories()));

    }
    @Async
    @JmsListener(destination = POST_OK_QUEUE)
    public void setCommentFindStatus(Long postId) {
        log.info("COMMENT_FIND - Finding comments for post -{}", postId);
        PostStateManager postStateManager = new PostStateManager(new Post(postId), POST_OK_QUEUE);
        try {
            List<CommentDto> commentFound = client.findCommentByPostId(postStateManager.getPostId());
            History history = postStateManager.handleState();
            historyRepository.save(history);
            setCommentOkStatus(postStateManager, commentFound);
        } catch (Exception e) {
            History historyFailed = postStateManager.handleFailed();
            historyRepository.save(historyFailed);
            setFailedStatus(postStateManager);
        }

    }
    @Async
    public void setCommentOkStatus(PostStateManager postStateManager, List<CommentDto> commentFound) {
        log.info("COMMENT_OK - Comments founded for post -{}", postStateManager.getPostId());
        History history = postStateManager.handleState();
        historyRepository.save(history);
        commentRepository.saveAll(commentFound.stream().
                map(commentDto -> new Comment(commentDto, postStateManager.getPostId())).toList());
        setEnabledStatus(postStateManager);
    }
    @Async
    public void setEnabledStatus(PostStateManager postStateManager) {
        log.info("ENABLED - Enabled post -{}", postStateManager.getPostId());
        History history = postStateManager.handleState();
        historyRepository.save(history);
    }
    @Async
    @JmsListener(destination = FAILED_QUEUE)
    public void failedStatusListener(History history)  {
        PostStateManager postStateManager = new PostStateManager(
                new Post(history.getPostId(), List.of(history)), FAILED_QUEUE);
        setFailedStatus(postStateManager);
    }
    @Async
    public void setFailedStatus(PostStateManager postStateManager) {
        log.info("FAILED - Failed to found post/comments -{}", postStateManager.getPostId());
        History history = postStateManager.handleState();
        historyRepository.save(history);
        setDisabledStatus(postStateManager);
    }
    @Async
    public void setDisabledStatus(PostStateManager postStateManager) {
        log.info("DISABLED - Disabled post -{}", postStateManager.getPostId());
        History history = postStateManager.handleState();
        historyRepository.save(history);
    }
    @Async
    public void setUpdatingStatus(PostStateManager postStateManager) {
        log.info("UPDATING - Updating post -{}", postStateManager.getPostId());
        History history = postStateManager.handleState();
        historyRepository.save(history);
        setPostFindStatus(postStateManager);
    }
}
