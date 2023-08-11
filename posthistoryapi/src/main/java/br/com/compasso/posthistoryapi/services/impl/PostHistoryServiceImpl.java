package br.com.compasso.posthistoryapi.services.impl;
import br.com.compasso.posthistoryapi.client.PostClient;
import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.DuplicatePostException;
import br.com.compasso.posthistoryapi.exceptions.HistoryNotFoundException;
import br.com.compasso.posthistoryapi.exceptions.PostNotFoundException;
import br.com.compasso.posthistoryapi.manager.PostManager;
import br.com.compasso.posthistoryapi.message.publisher.MessageProducer;
import br.com.compasso.posthistoryapi.repositories.CommentRepository;
import br.com.compasso.posthistoryapi.repositories.HistoryRepository;
import br.com.compasso.posthistoryapi.repositories.PostRepostory;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.compasso.posthistoryapi.constants.GlobalConstants.*;


@Service
@RequiredArgsConstructor
public class PostHistoryServiceImpl implements PostHistoryService {

  private final PostClient client;
  private final HistoryRepository historyRepository;
  private final PostRepostory postRepostory;
  private final CommentRepository commentRepository;
  private final MessageProducer mqProducer;

  @Override
  public void process(Long postId) {
    if (historyRepository.existsByPostId(postId)) {
        throw new DuplicatePostException("");
    }
    mqProducer.sendMessage(PROCESS_QUEUE, postId);
  }


  @Override
  public void disable(Long postId) {
    if (!historyRepository.existsByPostId(postId)) {
      throw new HistoryNotFoundException("");
    }
    mqProducer.sendMessage(DISABLE_QUEUE, postId);
  }

  @Override
  public void reprocess(Long postId) {
    if (!historyRepository.existsByPostId(postId)) {
      throw new HistoryNotFoundException("");
    }
    mqProducer.sendMessage(REPROCESS_QUEUE, postId);
  }

  @Override
  public List<PostDtoResponse> findAll() {
    return postRepostory.findAll().parallelStream().map(PostDtoResponse::new).toList();
  }

  @JmsListener(destination = PROCESS_QUEUE)
  private void createPostHistoryChain(Long postId){
    PostManager postManager = new PostManager(postId);
    createHistoryStatus(postManager);
  }
  @JmsListener(destination = REPROCESS_QUEUE)
  private void updatePostHistoryChain(Long postId){
    postRepostory.findById(postId).ifPresentOrElse(post ->
      setUpdatingStatus(new PostManager(post)), () -> {throw new PostNotFoundException("");});
  }
  @JmsListener(destination = DISABLE_QUEUE)
  private void disablePostHistoryChain(Long postId){
    postRepostory.findById(postId).ifPresentOrElse(post ->
      setDisabledStatus(new PostManager(post)), () -> {throw new PostNotFoundException("");});
  }
  private void createHistoryStatus(PostManager postManager){
    postRepostory.save(new Post(postManager.getPostId()));
    History history = historyRepository.save(new History(Status.CREATED, postManager.getPostId()));
    postManager.handleState(history);
    setPostFindStatus(postManager);
  }

  private void setPostFindStatus(PostManager postManager) {
    Long postId = postManager.getPostId();
    History history = new History(Status.POST_FIND, postId);
    postManager.handleState(history);
    historyRepository.save(history);
    client.findPostById(postId)
        .ifPresentOrElse(postFound -> setPostOkStatus(postManager, postFound),
          () -> setFailedStatus(postManager));
  }

  private void setPostOkStatus(PostManager postManager, PostDto postFound) {
    postManager.setPost(postFound);
    History history = historyRepository.save(new History(Status.POST_OK, postManager.getPostId()));
    postManager.handleState(history);
    postRepostory.save(postManager.toPostEntity());
    setCommentFindStatus(postManager);
  }

  private void setCommentFindStatus(PostManager postManager) {
    Long postId = postManager.getPostId();
    History history = new History(Status.COMMENTS_FIND, postId);
    postManager.handleState(history);
    historyRepository.save(history);
    client.findCommentByPostId(postId)
        .ifPresentOrElse(commentFound -> setCommentOkStatus(postManager, commentFound),
          () -> setFailedStatus(postManager));
  }

  private void setCommentOkStatus(PostManager postManager, List<CommentDto> commentFound) {
    postManager.setComments(commentFound);
    History history = new History(Status.COMMENTS_OK, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    commentRepository.saveAll(postManager.toCommentEntity());
     setEnabledStatus(postManager);
  }

  private void setEnabledStatus(PostManager postManager) {
    History history = new History(Status.ENABLED, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
  }

  private void setFailedStatus(PostManager postManager) {
    History history = new History(Status.FAILED, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    setDisabledStatus(postManager);
  }

  private void setDisabledStatus(PostManager postManager) {
    History history = new History(Status.DISABLED, postManager.getPostId());
    postManager.handleDisabled(history);
    historyRepository.save(history);
  }

  private void setUpdatingStatus(PostManager postManager) {
    History history = new History(Status.UPDATING, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    setPostFindStatus(postManager);
  }
//  @JmsListener(destination = SAVE_HISTORY_QUEUE)
//  private void saveHistory(History history){
//    historyRepository.save(history);

}


