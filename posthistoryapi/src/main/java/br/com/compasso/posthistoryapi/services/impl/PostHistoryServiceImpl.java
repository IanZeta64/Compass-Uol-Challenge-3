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
import br.com.compasso.posthistoryapi.repositories.CommentRepository;
import br.com.compasso.posthistoryapi.repositories.HistoryRepository;
import br.com.compasso.posthistoryapi.repositories.PostRepostory;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostHistoryServiceImpl implements PostHistoryService {

  private final PostClient client;
  private final HistoryRepository historyRepository;
  private final PostRepostory postRepostory;
  private final CommentRepository commentRepository;

  @Override
  public PostDtoResponse process(Long postId) {
    if (historyRepository.existsByPostId(postId)) {
        throw new DuplicatePostException("");
    }
    PostManager postManager = new PostManager(postId);
    return createPostHistoryChain(postManager).toResponse();
  }


  @Override
  public PostDtoResponse disable(Long postId) {
    if (!historyRepository.existsByPostId(postId)) {
      throw new HistoryNotFoundException("");
    }
    PostManager postManager = postRepostory.findById(postId).map(post ->
      setDisabledStatus(new PostManager(post)))
      .orElseThrow(() -> new PostNotFoundException(""));
    return  postManager.toResponse();
  }

  @Override
  public PostDtoResponse reprocess(Long postId) {
    if (!historyRepository.existsByPostId(postId)) {
      throw new HistoryNotFoundException("");
    }
    PostManager postManager = postRepostory.findById(postId).map(post ->
      updatePostHistoryChain(new PostManager(post))).orElseThrow(() -> new PostNotFoundException(""));
    return  postManager.toResponse();
  }

  @Override
  public List<PostDtoResponse> findAll() {
    return postRepostory.findAll().parallelStream().map(PostDtoResponse::new).toList();
  }
  private PostManager createPostHistoryChain(PostManager postManager){
    return createHistoryStatus(postManager);
  }

  private PostManager updatePostHistoryChain(PostManager postManager){
    return setUpdatingStatus(postManager);
  }
  private PostManager createHistoryStatus(PostManager postManager){
    postRepostory.save(new Post(postManager.getPostId()));
    History history = historyRepository.save(new History(Status.CREATED, postManager.getPostId()));
    postManager.handleState(history);
    return setPostFindStatus(postManager);
  }

  private PostManager setPostFindStatus(PostManager postManager) {
    Long postId = postManager.getPostId();
    History history = new History(Status.POST_FIND, postId);
    postManager.handleState(history);
    historyRepository.save(history);
    Optional<PostDto> request = client.findPostById(postId);
    if(request.isEmpty()) return setFailedStatus(postManager);
    postManager.setPost(request.get());
    return setPostOkStatus(postManager);
  }

  private PostManager setPostOkStatus(PostManager postManager) {
    History history = historyRepository.save(new History(Status.POST_OK, postManager.getPostId()));
    postManager.handleState(history);
    postRepostory.save(postManager.toEntity());
    return setCommentFindStatus(postManager);
  }

  private PostManager setCommentFindStatus(PostManager postManager) {
    Long postId = postManager.getPostId();
    History history = new History(Status.COMMENTS_FIND, postId);
    postManager.handleState(history);
    historyRepository.save(history);
    Optional<List<CommentDto>> request = client.findCommentByPostId(postId);
    if(request.isEmpty()) return setFailedStatus(postManager);
    postManager.setComments(request.get());
    return setCommentOkStatus(postManager);
  }

  private PostManager setCommentOkStatus(PostManager postManager) {
    History history = new History(Status.COMMENTS_OK, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    commentRepository.saveAll(postManager.toCommentEntity());
    return setEnabledStatus(postManager);
  }

  private PostManager setEnabledStatus(PostManager postManager) {
    History history = new History(Status.ENABLED, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    return postManager;
  }

  private PostManager setFailedStatus(PostManager postManager) {
    History history = new History(Status.FAILED, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    return setDisabledStatus(postManager);
  }

  private PostManager setDisabledStatus(PostManager postManager) {
    History history = new History(Status.DISABLED, postManager.getPostId());
    postManager.handleDisabled(history);
    historyRepository.save(history);
    return postManager;
  }

  private PostManager setUpdatingStatus(PostManager postManager) {
    History history = new History(Status.UPDATING, postManager.getPostId());
    postManager.handleState(history);
    historyRepository.save(history);
    return setPostFindStatus(postManager);
  }


}


//    History historyCreated = historyRepository.save(new History(Status.CREATED, postId));
//    return new PostManager(historyCreated);
//  }
//
//  private PostManager setEnabled(Long postId, PostManager response) {
//    if(!response.getHistories().contains(Status.POST_OK) && response.getHistories().contains(Status.COMMENTS_OK)){
//      throw new RuntimeException();
//    }
//    History history = historyRepository.save(new History(Status.ENABLED, postId));
//    response.addHistory(history);
//    return response;
//  }
//
//  private PostManager commentFind(Long postId, PostManager postResponse) {
//   return client.findCommentByPostId(postId)
//     .map(comment -> {
//       postResponse.addComment(comment);
//       postResponse.addHistory(historyRepository.save(new History(Status.COMMENTS_OK, postId)));
//       return  postResponse;
//     }).orElseGet(() ->  {
//       postResponse.addHistory(historyRepository.save(new History(Status.FAILED, postId)));
//       throw new RuntimeException();
//     });
//  }
//
//  private PostManager postFind(Long postId, PostManager postResponse) {
//    return client.findPostById(postId)
//      .map(post -> {
//        postResponse.setPost(post);
//        postResponse.addHistory(historyRepository.save(new History(Status.POST_OK, postId)));
//        return  postResponse;
//      }).orElseGet(() ->  {
//        postResponse.addHistory(historyRepository.save(new History(Status.FAILED, postId)));
//        throw new RuntimeException();
//      });
//  }


