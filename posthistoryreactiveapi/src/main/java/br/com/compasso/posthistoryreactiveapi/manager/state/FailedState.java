//package br.com.compasso.posthistoryreactiveapi.manager.state;
//
//import br.com.compasso.posthistoryreactiveapi.entity.History;
//import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
//import reactor.core.publisher.Mono;
//
//public class FailedState extends PostStateImpl{
//
//  @Override
//  public Mono<Void> handleState(PostManager postManager, History history) {
//    return Mono.fromRunnable(() -> super.handleDisabled(postManager, history));
//  }
//}
