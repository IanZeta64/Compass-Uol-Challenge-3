package br.com.compasso.posthistoryreactiveapi.manager.state;

import br.com.compasso.posthistoryreactiveapi.constants.GlobalConstants;
import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.entity.Post;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import br.com.compasso.posthistoryreactiveapi.manager.classes.*;
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
public class PostStateManager implements Serializable {
  private Long postId;
  private List<History> histories;
  private PostState state;

  public PostStateManager(Long postId) {
    this.postId = postId;
    this.histories = new ArrayList<>();
    this.state = new CreatedState();
  }
  public PostStateManager(Post post, String queue) {
    this.postId = post.getPostId();
    this.histories = post.getHistories();
    if (queue.equals(GlobalConstants.REPROCESS_QUEUE))
      this.state = new UpdatingState();
    if (queue.equals(GlobalConstants.DISABLE_QUEUE))
      this.state = new DisableState();
    if(queue.equals(GlobalConstants.POST_OK_QUEUE))
      this.state = new CommentFindState();
    if(queue.equals(GlobalConstants.FAILED_QUEUE))
      this.state = new FailedState();
  }
  public void addHistory(History history){
    this.histories.add(history);
  }


  public void setState(PostState state) {this.state = state;}
  public Mono<History> handleState() {
    return Mono.defer(() -> state.handleState(this));
  }

  public Status getLastHistoryStatus(){
    return this.histories.get(this.histories.size()-1).getStatus();
  }
  public Mono<History> handleFailed() {
    return Mono.defer(() -> state.handleDisabled(this));
  }



}
