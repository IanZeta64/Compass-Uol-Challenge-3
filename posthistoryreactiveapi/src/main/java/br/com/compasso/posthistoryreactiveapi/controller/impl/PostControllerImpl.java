package br.com.compasso.posthistoryreactiveapi.controller.impl;

import br.com.compasso.posthistoryreactiveapi.controller.PostController;
import br.com.compasso.posthistoryreactiveapi.response.PostResponse;
import br.com.compasso.posthistoryreactiveapi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostControllerImpl implements PostController {

  private final PostService service;
  @Override
  public Mono<ResponseEntity<Mono<Void>>> process(Long id)  {
    return Mono.defer(() ->
      service.process(id).subscribeOn(Schedulers.parallel())
        .map(response -> ResponseEntity.status(201).body(Mono.just(response)))
        .doOnError(err -> log.error("Error during process post - {}", err.getMessage()))
        .doOnNext(it -> log.info("Post process with sucess - {}", it)));
  }

  @Override
  public Mono<ResponseEntity<Mono<Void>>> disable(Long id) {
    return Mono.defer(() ->
      service.disable(id).subscribeOn(Schedulers.parallel())
        .map(response -> ResponseEntity.status(204).body(Mono.just(response)))
        .doOnError(err -> log.error("Error during reprocess post - {}", err.getMessage()))
        .doOnNext(it -> log.info("Post reprocess with sucess - {}", it)));
  }

  @Override
  public Mono<ResponseEntity<Mono<Void>>> reprocess(Long id) {
    return Mono.defer(() ->
      service.reprocess(id).subscribeOn(Schedulers.parallel())
        .map(response -> ResponseEntity.status(204).body(Mono.just(response)))
        .doOnError(err -> log.error("Error during disable post - {}", err.getMessage()))
        .doOnNext(it -> log.info("Post disable with sucess - {}", it)));
  }

  @Override
  public Mono<ResponseEntity<Flux<PostResponse>>> findAll() {
    return Flux.defer(service::findAll).subscribeOn(Schedulers.parallel()).collectList()
      .map(apostasResponse -> ResponseEntity.ok().body(Flux.fromIterable(apostasResponse)))
      .doOnError(err -> log.error("Error to find all posts - {}", err.getMessage()))
      .doOnNext(it -> log.info("All posts found with sucess - {}", it));
  }
}
