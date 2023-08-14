package br.com.compasso.posthistoryapi.state;
import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.state.classes.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.scheduling.annotation.Async;

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
    this.postId = post.getId();
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
  @Async
  public History handleState() {
   return state.handleState(this);
  }

  public Status getLastHistoryStatus(){
    return this.histories.get(this.histories.size()-1).getStatus();
  }
  @Async
  public History handleFailed() {
    return state.handleDisabled(this);
  }



}
