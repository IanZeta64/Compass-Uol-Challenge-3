package br.com.compasso.posthistoryapi.statemanager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.statemanager.PostManager;

public class PostFindState implements PostState {
  @Override
  public History handleState(PostManager postManager) {
    var hist = new History(Status.POST_FIND, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new PostOkState());
    return hist;
  }

  @Override
  public History handleDisable(PostManager postManager) {
    var hist = new History(Status.POST_FIND, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new FailedState());
    return hist;
  }
//  @Serial
//  private static final long serialVersionUID = GlobalConstants.POST_STATE_POST_FIND_serialVersionUID;
//  @Override
//  public void handleState(PostManager postManager, History history) {
//    if (!history.getStatus().equals(Status.POST_FIND)) {
//      throw new ChangeStatusHistoryException("");
//    }
//    postManager.addHistory(history);
//    postManager.setState(new PostOkState());
//  }
}

