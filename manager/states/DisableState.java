package br.com.compasso.posthistoryapi.statemanager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.statemanager.PostManager;

public class DisableState implements PostState {
  @Override
  public History handleState(PostManager postManager) {
    var hist = new History(Status.DISABLED, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new UpdatingState());
    return hist;
  }

  @Override
  public History handleDisable(PostManager postManager) {
    throw new ChangeStatusHistoryException("");
  }
//  @Serial
//  private static final long serialVersionUID = GlobalConstants.POST_STATE_DISABLED_serialVersionUID;
//  @Override
//  public void handleState(PostManager postManager, History history) {
//    if (!history.getStatus().equals(Status.DISABLED)) {
//      throw new ChangeStatusHistoryException("");
//    }
//    postManager.addHistory(history);
//    postManager.setState(new UpdatingState());
//  }
}
