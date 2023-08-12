package br.com.compasso.posthistoryreactiveapi.manager.states;
import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;

public class UpdatingState extends PostStateService {
  @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.UPDATING)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new PostFindState());
  }

}
