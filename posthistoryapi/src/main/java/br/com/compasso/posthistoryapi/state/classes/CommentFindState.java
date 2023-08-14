package br.com.compasso.posthistoryapi.state.classes;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.state.PostStateManager;
import org.springframework.scheduling.annotation.Async;

public class CommentFindState implements PostState {
  @Async
  @Override
  public History handleState(PostStateManager postStateManager) {
    var hist = new History(Status.COMMENTS_FIND, postStateManager.getPostId());
    postStateManager.addHistory(hist);
    postStateManager.setState(new CommentOkState());
    return hist;
  }
  @Async
  @Override
  public History handleDisabled(PostStateManager postStateManager) {
    var hist = new History(Status.COMMENTS_FIND, postStateManager.getPostId());
    postStateManager.setState(new FailedState());
    postStateManager.addHistory(hist);
    return hist;
  }
}
