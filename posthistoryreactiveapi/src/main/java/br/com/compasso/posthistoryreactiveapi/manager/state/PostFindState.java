//package br.com.compasso.posthistoryreactiveapi.manager.state;
//
//import br.com.compasso.posthistoryreactiveapi.entity.History;
//import br.com.compasso.posthistoryreactiveapi.enums.Status;
//import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
//import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
//import reactor.core.publisher.Mono;
//
//public class PostFindState extends PostStateImpl {
//
//  @Override
//  public Mono<Void> handleState(PostManager postManager, History history) {
//    return Mono.defer(() -> {
//      if (!history.getStatus().equals(Status.POST_FIND)) {
//        return Mono.error(() -> new ChangeStatusHistoryException(""));
//      }
//      postManager.addHistory(history);
//      postManager.setState(new PostOkState());
//      return Mono.empty();
//    });
//  }
//}
