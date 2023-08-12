package br.com.compasso.posthistoryreactiveapi.manager.states;


import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;

import java.io.Serial;
import java.io.Serializable;

public class PostStateService implements PostState {

  @Override
  public void handleState(PostManager post, History history) {
    post.setState(new CreatedState());
  }

  @Override
  public void handleDisabled(PostManager post, History history) {
    if (history.getStatus().equals(Status.FAILED)
      || history.getStatus().equals(Status.DISABLED)) {
      post.addHistory(history);
      post.setState(new DisableState());
      return;
    }
    throw new ChangeStatusHistoryException("");
  }
}
