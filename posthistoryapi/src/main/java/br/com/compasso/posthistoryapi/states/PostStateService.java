package br.com.compasso.posthistoryapi.services.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.responses.HistoryDtoResponse;
import br.com.compasso.posthistoryapi.responses.PostDtoResponse;

public class PostStateService implements PostState{
  @Override
  public void handleState(PostDtoResponse post, HistoryDtoResponse history) {
    post.addHistory(history);
    post.setState(new CreatedState());
  }

  @Override
  public void handleError(PostDtoResponse post, HistoryDtoResponse history) {
    if (!history.status().equals(Status.FAILED)) {
      throw new ChangeStatusHistoryException("");
    }
    post.addHistory(history);
    post.setState(new FailedState());
  }
}
