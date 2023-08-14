package br.com.compasso.posthistoryapi.client.Dto;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.entity.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class CommentDto  {


  @JsonProperty("id")
  private Long id;
  @JsonProperty("body")
  private String body;

  public CommentDto(Comment comment) {
    this.id = comment.getId();
    this.body = comment.getBody();
  }
}

