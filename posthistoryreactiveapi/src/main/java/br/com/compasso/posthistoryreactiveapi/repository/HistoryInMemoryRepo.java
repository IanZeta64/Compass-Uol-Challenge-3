package br.com.compasso.posthistoryreactiveapi.repository;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HistoryInMemoryRepo {

  private List<History> historyList = new ArrayList<>();

  public Mono<History> save(History history){
    this.historyList.add(history);
    return Mono.just(history);
  }
}
