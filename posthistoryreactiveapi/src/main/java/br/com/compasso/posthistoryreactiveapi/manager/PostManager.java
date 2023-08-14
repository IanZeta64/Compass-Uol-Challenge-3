package br.com.compasso.posthistoryreactiveapi.manager;

import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.entity.Post;
import br.com.compasso.posthistoryreactiveapi.enums.Status;

import br.com.compasso.posthistoryreactiveapi.manager.states.CreatedState;
import br.com.compasso.posthistoryreactiveapi.manager.states.PostState;
import br.com.compasso.posthistoryreactiveapi.manager.states.UpdatingState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Mono;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@ToString
public class PostManager implements Serializable {

  private Long postId;
  @Getter
  private List<History> histories;
  private PostState state;

  public PostManager(Long postId) {
    this.postId = postId;
    this.state = new CreatedState();
    this.histories = new ArrayList<>();
  }
  public PostManager(Post post) {
    this.postId = post.getId();
    Status status = post.getHistories().get(post.getHistories().size()-1).getStatus();
    if (status.equals(Status.ENABLED) || status.equals(Status.DISABLED))
      this.state = new UpdatingState();
    this.histories = post.getHistories();
  }
  public void setState(PostState state) {this.state = state;}
  public void handleState(History history) {
    state.handleState(this, history);
  }
  public void handleDisabled(History history) {
    state.handleDisabled(this, history);
  }

  public void addHistory(History history) {
    histories.add(history);
  }


}

