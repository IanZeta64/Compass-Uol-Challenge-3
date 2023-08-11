package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;

import java.io.Serial;
import java.io.Serializable;

public class DisableState extends PostStateService implements Serializable {
  @Serial
  private static final long serialVersionUID = GlobalConstants.POST_STATE_DISABLED_serialVersionUID;
  @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.DISABLED)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new UpdatingState());
  }
}
