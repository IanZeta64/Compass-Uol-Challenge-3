package br.com.compasso.posthistoryapi.state.classes;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.exceptionclass.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.state.PostStateManager;
import org.springframework.scheduling.annotation.Async;

public class PostOkState implements PostState {
  @Async
  @Override
  public History handleState(PostStateManager postStateManager) {
    var hist = new History(Status.POST_OK, postStateManager.getPostId());
    postStateManager.addHistory(hist);
    postStateManager.setState(new CommentFindState());
    return hist;
  }
  @Async
  @Override
  public History handleDisabled(PostStateManager postStateManager) {
    throw new ChangeStatusHistoryException("Error during change status of post %s.");
  }

}
