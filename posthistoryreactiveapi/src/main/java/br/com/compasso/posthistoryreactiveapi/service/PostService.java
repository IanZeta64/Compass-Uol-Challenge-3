package br.com.compasso.posthistoryreactiveapi.service;

import br.com.compasso.posthistoryreactiveapi.response.PostResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostService {

  Mono<Void> process(Long id) ;
  Mono<Void> disable(Long id);
  Mono<Void> reprocess(Long id);
  Flux<PostResponse> findAll();
}
