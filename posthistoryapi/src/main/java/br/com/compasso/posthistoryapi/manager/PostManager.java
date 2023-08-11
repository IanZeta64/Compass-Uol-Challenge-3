package br.com.compasso.posthistoryapi.manager;

import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.Comment;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.manager.states.*;
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
    this.histories.add(history);
  }

//  public void setComments(List<CommentDto> comments){
//    this.comments = comments;
//  }
//  public void setPost(PostDto post){
//    this.post = post;
//  }


//  public Post toPostEntity(){
//   return new Post(this.postId, this.post.getTitle(), this.post.getBody(),
//      this.comments.stream().map(commentDto ->
//        new Comment(commentDto.getId(),
//        commentDto.getBody(),
//          postId)).toList(),
//      this.histories);
//  }
//  public List<Comment> toCommentEntity() {
//    return this.comments.stream().map(commentDto ->
//      new Comment(commentDto.getId(),
//        commentDto.getBody(),
//        postId)).toList();
//  }

}
