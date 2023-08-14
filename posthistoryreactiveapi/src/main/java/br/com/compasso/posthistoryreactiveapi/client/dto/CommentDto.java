package br.com.compasso.posthistoryreactiveapi.client.dto;

import br.com.compasso.posthistoryreactiveapi.entity.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentDto(@JsonProperty("id")
                         Long commentId,
                         @JsonProperty("body")
                         String body) {
  public CommentDto(Comment comment) {
    this(comment.getCommentId(), comment.getBody());
  }
}
