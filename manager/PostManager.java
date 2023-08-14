package br.com.compasso.posthistoryapi.statemanager;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.statemanager.states.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@ToString
public class PostManager implements Serializable {
  @Serial
  private static final long serialVersionUID = GlobalConstants.POST_MANAGER_serialVersionUID;
  private Long postId;
  private PostState state;
  @Getter
  private List<History> histories;

  public PostManager(Long postId) {
    this.postId = postId;
    this.state = new CreatedState();
    this.histories = new ArrayList<>();
  }
  public PostManager(Long postId, Byte bytes) {
    this.postId = postId;
    this.state = new CreatedState();
    this.histories = new ArrayList<>();
  }
  public PostManager(Post post) {
    this.postId = getPostId();
    Status status = post.getHistories().get(post.getHistories().size()-1).getStatus();
//    if (status.equals(Status.DISABLED || status.equals(Status.ENABLED)))
    this.state = new UpdatingState();
//    else this.state = new UpdatingState();
      this.histories = post.getHistories();
  }
  public void setState(PostState state) {this.state = state;}
  public History handleState() {
    return state.handleState(this);
  }
  public History handleDisabled() {
    return state.handleDisable(this);
  }

  public void addHistory(History history) {this.histories.add(history); }
}
