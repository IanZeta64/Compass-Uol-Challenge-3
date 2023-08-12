package br.com.compasso.posthistoryreactiveapi.manager.states;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;

import java.io.Serial;
import java.io.Serializable;

public class CommentOkState extends PostStateService implements Serializable {
 @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.COMMENTS_OK)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new EnabledState());
  }
}
