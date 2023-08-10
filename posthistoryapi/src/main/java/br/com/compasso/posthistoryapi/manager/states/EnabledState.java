package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;

public class EnabledState extends PostStateService {
  @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.ENABLED)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new UpdatingState());
  }

//  @Override
//  public void handleDisabled(PostManager postManager, History history) {
////    if (!history.getStatus().equals(Status.ENABLED)) {
////      throw new ChangeStatusHistoryException("");
////    }
////    postManager.addHistory(history);
////    postManager.setState(new DisableState());
//    super.handleDisabled(postManager,history);
//  }
}
