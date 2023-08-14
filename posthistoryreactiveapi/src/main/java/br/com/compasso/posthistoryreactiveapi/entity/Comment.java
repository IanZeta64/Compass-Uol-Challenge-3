package br.com.compasso.posthistoryreactiveapi.entity;

import br.com.compasso.posthistoryreactiveapi.client.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


//@Entity

  @Document
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public class Comment {

    @Id
    private String id;
    @Indexed(unique = true)
    private Long commentId;
    private String body;
    private Long postId;

    public Comment(CommentDto commentDto, Long postId) {
      this.commentId = commentDto.commentId();
      this.body = commentDto.body();
      this.postId = postId;
    }

  }
