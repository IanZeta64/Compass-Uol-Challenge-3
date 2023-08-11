package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;

import java.io.Serial;
import java.io.Serializable;

public class PostStateService implements PostState, Serializable {
  @Serial
  private static final long serialVersionUID = GlobalConstants.POST_STATE_SERVICE_serialVersionUID;
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
