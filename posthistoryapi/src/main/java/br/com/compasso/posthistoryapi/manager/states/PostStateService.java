package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.manager.PostManager;

public class PostStateService implements PostState{
  @Override
  public void handleState(PostManager post, History history) {
    post.addHistory(history);
    post.setState(new CreatedState());
  }

//  @Override
//  public void handleError(PostManager post, History history) {
//    if (!history.getStatus().equals(Status.FAILED)) {
//      throw new ChangeStatusHistoryException("");
//    }
//    post.addHistory(history);
//    post.setState(new DisableState());
//  }
}
