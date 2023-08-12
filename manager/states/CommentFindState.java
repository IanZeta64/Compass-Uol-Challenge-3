package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.manager.PostManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentFindState implements PostState {
//  @Serial
//  private static final long serialVersionUID = POST_STATE_COMMENT_FIND_serialVersionUID;

//  @Override
//  public void handleState(PostManager postManager, History history) {
//    if (!history.getStatus().equals(Status.COMMENTS_FIND)) {
//      throw new ChangeStatusHistoryException("");
//    }
//    postManager.addHistory(history);
//    postManager.setState(new CommentOkState());
//
//  }

  @Override
  public History handleState(PostManager postManager) {
    var hist = new History(Status.COMMENTS_FIND, postManager.getPostId());
    postManager.addHistory(hist);
    postManager.setState(new CommentOkState());
    return hist;
  }

  @Override
  public History handleDisable(PostManager postManager) {
    var hist = new History(Status.COMMENTS_FIND, postManager.getPostId());
    postManager.setState(new FailedState());
    postManager.addHistory(hist);
    return hist;
  }
}
