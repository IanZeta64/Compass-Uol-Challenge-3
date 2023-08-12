package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;

public class CommentOkState implements PostState {
  @Override
  public History handleState(PostManager postManager) {
      var hist = new History(Status.COMMENTS_OK, postManager.getPostId());
      postManager.addHistory(hist);
      postManager.setState(new EnabledState());
      return hist;
  }
  @Override
  public History handleDisable(PostManager postManager) {
    throw new ChangeStatusHistoryException("");
  }
//  @Serial
//  private static final long serialVersionUID = GlobalConstants.POST_STATE_COMMENT_OK_serialVersionUID;
//  @Override
//  public void handleState(PostManager postManager, History history) {
//    if (!history.getStatus().equals(Status.COMMENTS_OK)) {
//      throw new ChangeStatusHistoryException("");
//    }
//    postManager.addHistory(history);
//    postManager.setState(new EnabledState());
//  }
}
