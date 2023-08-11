package br.com.compasso.posthistoryapi.manager.states;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.exceptions.ChangeStatusHistoryException;
import br.com.compasso.posthistoryapi.manager.PostManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@RequiredArgsConstructor
public class CommentFindState extends PostStateService implements Serializable {
  @Serial
  private static final long serialVersionUID = GlobalConstants.POST_STATE_COMMENT_FIND_serialVersionUID;

  @Override
  public void handleState(PostManager postManager, History history) {
    if (!history.getStatus().equals(Status.COMMENTS_FIND)) {
      throw new ChangeStatusHistoryException("");
    }
    postManager.addHistory(history);
    postManager.setState(new CommentOkState());

  }
}
