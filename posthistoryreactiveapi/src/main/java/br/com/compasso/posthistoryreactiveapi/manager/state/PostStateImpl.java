//package br.com.compasso.posthistoryreactiveapi.manager.state;
//
//import br.com.compasso.posthistoryreactiveapi.entity.History;
//import br.com.compasso.posthistoryreactiveapi.enums.Status;
//import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
//import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
//
//import br.com.compasso.posthistoryreactiveapi.manager.states.CreatedState;
//import br.com.compasso.posthistoryreactiveapi.manager.states.PostState;
//import reactor.core.publisher.Mono;
//
//
//public class PostStateImpl implements PostState {
//
//
//  @Override
//  public void handleState(PostManager post, History history) {
//    return Mono.fromRunnable(() -> post.setState(new CreatedState()));
//  }
//
//  @Override
//  public Mono<Void> handleDisabled(PostManager post, History history) {
//    return Mono.defer(() -> {
//      if (history.getStatus().equals(Status.FAILED)
//        || history.getStatus().equals(Status.DISABLED)) {
//        post.addHistory(history);
//        post.setState(new DisabledState());
//        return Mono.empty();
//      }
//      return Mono.error(new ChangeStatusHistoryException(""));
//    });
//  }
//}
//
