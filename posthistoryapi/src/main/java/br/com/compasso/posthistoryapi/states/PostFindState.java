package br.com.compasso.posthistoryapi.services.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.responses.HistoryDtoResponse;
import br.com.compasso.posthistoryapi.responses.PostDtoResponse;

public class PostFindState extends PostStateService {
  @Override
  public void handleState(PostDtoResponse postDtoResponse, HistoryDtoResponse history) {
    if (!history.status().equals(Status.POST_FIND)) {
      throw new ChangeStatusHistoryException("");
    }
    postDtoResponse.addHistory(history);
    postDtoResponse.setState(new PostOkState());
  }
}

