package br.com.compasso.posthistoryreactiveapi.manager.classes;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryreactiveapi.manager.state.PostStateManager;
import reactor.core.publisher.Mono;

public class FailedState implements PostState {
  @Override
  public Mono<History> handleState(PostStateManager postStateManager) {
    Status lastStatus = postStateManager.getLastHistoryStatus();
    if (lastStatus.equals(Status.COMMENTS_FIND) || lastStatus.equals(Status.POST_FIND)) {
      var hist = new History(Status.FAILED, postStateManager.getPostId());
      postStateManager.addHistory(hist);
      postStateManager.setState(new DisableState());
      return Mono.just(hist);
    }
    return Mono.error(new ChangeStatusHistoryException(""));
  }
  @Override
  public Mono<History> handleDisabled(PostStateManager postStateManager) {
    return Mono.error(new ChangeStatusHistoryException(""));
  }
}
