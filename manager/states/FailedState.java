package br.com.compasso.posthistoryapi.statemanager.states;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.statemanager.PostManager;

public class FailedState implements PostState {
  @Override
  public History handleState(PostManager postManager) {
    var hist = new History(Status.FAILED, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new DisableState());
    return hist;
  }

  @Override
  public History handleDisable(PostManager postManager) {
    throw new ChangeStatusHistoryException("");
  }
//  @Serial
//  private static final long serialVersionUID = GlobalConstants.POST_STATE_FAILED_serialVersionUID;
//  @Override
//  public void handleState(PostManager postManager, History history) {
//    super.handleDisabled(postManager, history);
//  }
}
