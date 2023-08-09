package br.com.compasso.posthistoryapi.repositories;

import br.com.compasso.posthistoryapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepostory extends JpaRepository<Post, Long> {

}
