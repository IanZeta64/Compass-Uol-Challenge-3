package br.com.compasso.posthistoryapi.repositories;

import br.com.compasso.posthistoryapi.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
  boolean existsByPostId(Long id);
}
