package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;

public class FailedState extends PostStateService {
  @Override
  public void handleState(PostManager postManager, History history) {
    super.handleDisabled(postManager, history);
  }
}
