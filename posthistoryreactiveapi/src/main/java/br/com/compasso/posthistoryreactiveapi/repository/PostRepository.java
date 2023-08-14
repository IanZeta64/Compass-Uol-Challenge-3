package br.com.compasso.posthistoryreactiveapi.repository;
import br.com.compasso.posthistoryreactiveapi.entity.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {

  Mono<Boolean> existsByPostId(Long postId);

  Mono<Post> findByPostId(Long postId);
}
