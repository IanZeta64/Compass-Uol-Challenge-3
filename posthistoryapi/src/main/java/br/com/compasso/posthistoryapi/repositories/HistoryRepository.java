package br.com.compasso.posthistoryapi.repositories;

import br.com.compasso.posthistoryapi.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
  boolean existsByPostId(Long id);
//  @Query("SELECT DISTINCT h.postId FROM History h")
//  List<Long> findAllPostIdByHistory();
//  List<History> findAllByPostId(Long postId);
}
