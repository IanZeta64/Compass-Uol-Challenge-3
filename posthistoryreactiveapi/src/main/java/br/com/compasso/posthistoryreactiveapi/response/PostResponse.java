package br.com.compasso.posthistoryreactiveapi.response;

import br.com.compasso.posthistoryreactiveapi.entity.Comment;
import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.entity.Post;

import java.util.List;

public record PostResponse (
  Long postid,
  String title,
  String body,
  List<Comment> comment,
  List<History> history
) {
  public PostResponse(Post post) {
    this(post.getPostId(), post
        .getTitle(), post.getBody(),
      post.getComments(),
      post.getHistories());
  }
}
