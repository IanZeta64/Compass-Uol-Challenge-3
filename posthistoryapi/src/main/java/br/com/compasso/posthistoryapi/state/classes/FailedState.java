package br.com.compasso.posthistoryapi.state.classes;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.exceptionclass.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.state.PostStateManager;
import org.springframework.scheduling.annotation.Async;

public class FailedState implements PostState {
  @Async
  @Override
  public History handleState(PostStateManager postStateManager) {
    Status lastStatus = postStateManager.getLastHistoryStatus();
    if(lastStatus.equals(Status.COMMENTS_FIND) || lastStatus.equals(Status.POST_FIND)){
    var hist = new History(Status.FAILED, postStateManager.getPostId());
    postStateManager.addHistory(hist);
    postStateManager.setState(new DisableState());
    return hist;
    }
    throw new ChangeStatusHistoryException("");
  }
  @Async
  @Override
  public History handleDisabled(PostStateManager postStateManager) {
    return this.handleState(postStateManager);
  }

}
