package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;

import java.io.Serial;
import java.io.Serializable;

public class PostFindState extends PostStateService implements Serializable {
  @Serial
  private static final long serialVersionUID = GlobalConstants.POST_STATE_POST_FIND_serialVersionUID;
  @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.POST_FIND)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new PostOkState());
  }
}

