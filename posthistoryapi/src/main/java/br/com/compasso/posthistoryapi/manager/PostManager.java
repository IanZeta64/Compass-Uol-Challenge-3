package br.com.compasso.posthistoryapi.manager;

import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.entity.Comment;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.manager.states.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@ToString
public class PostManager {
  private Long postId;
  private PostDto post;
  private List<CommentDto> comments;
  private List<History> histories;
  private PostState state;

  public PostManager(Long postId) {
    this.postId = postId;
    this.post = new PostDto();
    this.comments = new ArrayList<>();
    this.histories = new ArrayList<>();
    this.state = new CreatedState();
  }
  public PostManager(Post post) {
    this.postId = post.getId();
    this.post = new PostDto(post.getId(), post.getTitle(), post.getBody());
    this.comments = post.getComments().stream().map(CommentDto::new).toList();
    this.histories = post.getHistories();
    Status status = post.getHistories().get(post.getHistories().size()-1).getStatus();
    if (status.equals(Status.ENABLED) || status.equals(Status.DISABLED)) this.state = new UpdatingState();
  }
  public void addHistory(History history){
    this.histories.add(history);
  }
  public void addHistoryList(List<History> histories){
    this.histories.addAll(histories);
  }
  public void setComments(List<CommentDto> comments){
    this.comments = comments;
  }
  public void setPost(PostDto post){
    this.post = post;
  }
  public void setState(PostState state) {this.state = state;}
  public void handleState(History history) {
    state.handleState(this, history);
  }
  public void handleDisabled(History history) {
    state.handleDisabled(this, history);
  }


  public Post toEntity(){
   return new Post(this.postId, this.post.getTitle(), this.post.getBody(),
      this.comments.stream().map(commentDto ->
        new Comment(commentDto.getId(),
//        commentDto.getName(),
//        commentDto.getEmail(),
        commentDto.getBody(),
          postId)).toList(),
      this.histories);
  }
  public List<Comment> toCommentEntity(){
    return  this.comments.stream().map(commentDto ->
      new Comment(commentDto.getId(),
//      commentDto.getName(),
//      commentDto.getEmail(),
      commentDto.getBody(),
      postId)).toList();

  }
  public PostDtoResponse toResponse(){
      return  new PostDtoResponse(this.post, this.comments, this.histories);
  }


}
