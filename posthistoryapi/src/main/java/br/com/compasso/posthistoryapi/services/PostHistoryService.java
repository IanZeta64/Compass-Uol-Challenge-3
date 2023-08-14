package br.com.compasso.posthistoryapi.services;

import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.statemanager.PostStateManager;

import java.util.List;

public interface PostHistoryService {

  void process(Long id) ;
  void disable(Long id);
  void reprocess(Long id);
  List<PostDtoResponse> findAll();

  void createPostHistoryChain(Long postId);

  void updatePostHistoryChain(Long postId);

  void disablePostHistoryChain(Long postId);

  void createHistoryStatus(PostStateManager postStateManager);

  void setPostFindStatus(PostStateManager postStateManager);

  void setPostOkStatus(PostStateManager postStateManager, PostDto postFound);

  void setCommentFindStatus(Long postId);

  void setCommentOkStatus(PostStateManager postStateManager, List<CommentDto> commentFound);

  void setEnabledStatus(PostStateManager postStateManager);

  void failedStatusListener(History history);

  void setFailedStatus(PostStateManager postStateManager);

  void setDisabledStatus(PostStateManager postStateManager);

  void setUpdatingStatus(PostStateManager postStateManager);
}
