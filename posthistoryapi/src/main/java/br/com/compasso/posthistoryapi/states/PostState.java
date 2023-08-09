package br.com.compasso.posthistoryapi.services.states;


import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.responses.HistoryDtoResponse;
import br.com.compasso.posthistoryapi.responses.PostDtoResponse;

public interface PostState {

  void handleState(PostDtoResponse post, HistoryDtoResponse history);

  void handleError(PostDtoResponse post, HistoryDtoResponse history);
}
