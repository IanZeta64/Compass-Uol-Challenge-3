package br.com.compasso.posthistoryapi.state.classes;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.state.PostStateManager;
import org.springframework.scheduling.annotation.Async;

public class PostFindState implements PostState {
  @Async
  @Override
  public History handleState(PostStateManager postStateManager) {
    var hist = new History(Status.POST_FIND, postStateManager.getPostId());
    postStateManager.addHistory(hist);
    postStateManager.setState(new PostOkState());
    return hist;
  }
  @Async
  @Override
  public History handleDisabled(PostStateManager postStateManager) {
    var hist = new History(Status.POST_FIND, postStateManager.getPostId());
    postStateManager.addHistory(hist);
    postStateManager.setState(new FailedState());
    return hist;
  }
}

