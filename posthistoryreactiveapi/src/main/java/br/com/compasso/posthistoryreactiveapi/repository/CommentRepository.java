package br.com.compasso.posthistoryreactiveapi.repository;
import br.com.compasso.posthistoryreactiveapi.entity.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
}
