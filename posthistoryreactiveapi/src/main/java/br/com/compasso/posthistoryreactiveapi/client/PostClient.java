package br.com.compasso.posthistoryreactiveapi.client;
import br.com.compasso.posthistoryreactiveapi.client.dto.CommentDto;
import br.com.compasso.posthistoryreactiveapi.client.dto.PostDto;
import br.com.compasso.posthistoryreactiveapi.exceptions.PostNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static br.com.compasso.posthistoryreactiveapi.constants.GlobalConstants.POST_API_URL;

@Component
public class PostClient {
  private static final String URL = POST_API_URL;

  private final WebClient client;

  public PostClient(WebClient.Builder builder) {
    this.client = builder.baseUrl(URL).build();
  }

  public Mono<PostDto> findPostById(Long postId) {
    return client
      .get()
      .uri(String.format("/%s", postId))
      .exchangeToMono(result -> {
        if (result.statusCode().is2xxSuccessful()) {
          return result.bodyToMono(PostDto.class);
        } else {
          return Mono.error(new PostNotFoundException("Post not founded by id " + postId));
        }
      }).subscribeOn(Schedulers.boundedElastic());
  }

  public Flux<CommentDto> findCommentsByPostId(Long postId) {
    return client
      .get()
      .uri(String.format("/%s/comments", postId))
      .exchangeToFlux(result -> {
        if (result.statusCode().is2xxSuccessful()) {
          return result.bodyToFlux(CommentDto.class);
        } else {
          return Flux.error(new PostNotFoundException("Post not found by id " + postId));
        }
      }).subscribeOn(Schedulers.boundedElastic());
  }
}
