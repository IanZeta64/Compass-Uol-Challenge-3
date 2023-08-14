package br.com.compasso.posthistoryreactiveapi.manager.classes;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.manager.state.PostStateManager;
import reactor.core.publisher.Mono;

public interface PostState {


  Mono<History> handleState(PostStateManager postStateManager);

  Mono<History> handleDisabled(PostStateManager postStateManager);
}
