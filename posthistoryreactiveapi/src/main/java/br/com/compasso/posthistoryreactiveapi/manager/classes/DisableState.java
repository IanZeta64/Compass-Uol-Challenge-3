package br.com.compasso.posthistoryreactiveapi.manager.classes;


import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryreactiveapi.manager.state.PostStateManager;
import reactor.core.publisher.Mono;

public class DisableState implements PostState {
  @Override
  public Mono<History> handleState(PostStateManager postStateManager) {
    return Mono.defer(() -> {
      History hist = new History(Status.DISABLED, postStateManager.getPostId());
      postStateManager.addHistory(hist);
      postStateManager.setState(new UpdatingState());
      return Mono.just(hist);
    });
  }
  @Override
  public Mono<History> handleDisabled(PostStateManager postStateManager) {
    return Mono.error(new ChangeStatusHistoryException(""));
  }
}
