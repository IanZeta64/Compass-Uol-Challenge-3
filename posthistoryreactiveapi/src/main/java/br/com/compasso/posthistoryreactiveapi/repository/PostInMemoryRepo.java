package br.com.compasso.posthistoryreactiveapi.repository;

import br.com.compasso.posthistoryreactiveapi.entity.Post;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostInMemoryRepo {
  private List<Post> postList = new ArrayList<>();


  public Mono<Post> save(Post post){
    this.postList.add(post);
    return Mono.just(post);
  }

  public Mono<Boolean> existsById(Long postId){
   return Mono.just(this.postList.stream().anyMatch(post -> post.getId().equals(postId)));
  }

  public Mono<Post> findById(Long postId){
    return Mono.just(this.postList.stream().filter(post -> post.getId().equals(postId)).findAny().get());
  }
}
