package br.com.compasso.posthistoryapi.responses;

import br.com.compasso.posthistoryapi.client.requests.CommentDtoRequest;
import br.com.compasso.posthistoryapi.client.requests.PostDtoRequest;
import br.com.compasso.posthistoryapi.entity.History;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostResponse {
  private PostDtoRequest post;
  private List<CommentDtoRequest> comments;
  private List<History> histories;

  public PostResponse(History history) {
    this.comments = new ArrayList<>();
    this.histories = new ArrayList<>(List.of(history));
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
}
