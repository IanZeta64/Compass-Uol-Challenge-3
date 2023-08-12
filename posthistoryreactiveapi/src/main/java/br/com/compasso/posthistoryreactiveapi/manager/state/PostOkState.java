//package br.com.compasso.posthistoryreactiveapi.manager.state;
//
//import br.com.compasso.posthistoryreactiveapi.entity.History;
//import br.com.compasso.posthistoryreactiveapi.enums.Status;
//import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
//import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
//import reactor.core.publisher.Mono;
//
//public class PostOkState extends PostStateImpl {
//
//  @Override
//  public Mono<Void> handleState(PostManager postManager, History history) {
//    return Mono.defer(() -> {
//      if (!history.getStatus().equals(Status.POST_OK)) {
//        return Mono.error(() -> new ChangeStatusHistoryException(""));
//      }
//      postManager.addHistory(history);
//      postManager.setState(new CommentFindState());
//      return Mono.empty();
//    });
//  }
//}
