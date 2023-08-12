package br.com.compasso.posthistoryapi.manager.states;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.manager.PostManager;

public class UpdatingState implements PostState {
  @Override
  public History handleState(PostManager postManager) {
    var hist = new History(Status.UPDATING, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new PostFindState());
    return hist;
  }

  @Override
  public History handleDisable(PostManager postManager) {
    var hist = new History(Status.DISABLED, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new UpdatingState());
    return hist;
  }
//  @Serial
//  private static final long serialVersionUID = GlobalConstants.POST_STATE_UPDATING_serialVersionUID;
//  @Override
//  public void handleState(PostManager postManager, History history) {
//    if (!history.getStatus().equals(Status.UPDATING)) {
//      throw new ChangeStatusHistoryException("");
//    }
//    postManager.addHistory(history);
//    postManager.setState(new PostFindState());
//  }

}
