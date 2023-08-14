package br.com.compasso.posthistoryreactiveapi.repository;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends ReactiveMongoRepository<History, String> {

}
