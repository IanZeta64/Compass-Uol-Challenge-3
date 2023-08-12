package br.com.compasso.posthistoryreactiveapi.manager.states;


import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;
import java.io.Serializable;

public class FailedState extends PostStateService implements Serializable {
  @Override
  public void handleState(PostManager postManager, History history) {
    super.handleDisabled(postManager, history);
  }
}
