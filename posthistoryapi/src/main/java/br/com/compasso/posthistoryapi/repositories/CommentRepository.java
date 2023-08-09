package br.com.compasso.posthistoryapi.repositories;

import br.com.compasso.posthistoryapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
