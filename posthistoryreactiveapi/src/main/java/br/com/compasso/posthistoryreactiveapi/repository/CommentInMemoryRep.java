package br.com.compasso.posthistoryreactiveapi.repository;

import br.com.compasso.posthistoryreactiveapi.entity.Comment;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
@Repository
public class CommentInMemoryRep {
  private List<Comment> comments = new ArrayList<>();

  public Flux<Comment> saveAll(List<Comment>  comments){
    this.comments.addAll(comments);
    return Flux.fromIterable(comments);
  }
}
