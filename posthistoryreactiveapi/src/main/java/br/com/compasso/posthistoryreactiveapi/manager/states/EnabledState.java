package br.com.compasso.posthistoryreactiveapi.manager.states;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;

import java.io.Serializable;

public class EnabledState extends PostStateService implements Serializable {
 @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.ENABLED)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new UpdatingState());
  }

}
