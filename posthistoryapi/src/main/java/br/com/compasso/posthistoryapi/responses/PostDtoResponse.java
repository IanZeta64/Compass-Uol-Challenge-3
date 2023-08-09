package br.com.compasso.posthistoryapi.responses;

import br.com.compasso.posthistoryapi.client.requests.CommentDtoRequest;
import br.com.compasso.posthistoryapi.client.requests.PostDtoRequest;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.services.states.CreatedState;
import br.com.compasso.posthistoryapi.services.states.PostState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@ToString
public class PostResponse{
  private PostDtoRequest post;
  private List<CommentDtoRequest> comments;
  private List<History> histories;
  @JsonIgnore
  private PostState state;


  public PostResponse() {
    this.comments = new ArrayList<>();
    this.histories = new ArrayList<>();
    this.state = new CreatedState();
  }
  public void addHistory(History history){
    this.histories.add(history);
  }

  public void addComment(List<CommentDtoRequest> comments){
    this.comments.addAll(comments);
  }

  public void setPost(PostDtoRequest post){
    this.post = post;
  }
  public void setState(PostState state) {this.state = state;}
  public void handleState(Long postId, History history) {
    state.handleState(this, postId, history);
  }
  public void handleError(Long postId, History history) {
    state.handleError(this, postId, history);
  }
}
