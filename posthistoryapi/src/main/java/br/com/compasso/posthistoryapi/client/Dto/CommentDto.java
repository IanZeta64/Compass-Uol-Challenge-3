package br.com.compasso.posthistoryapi.client.Dto;

import br.com.compasso.posthistoryapi.entity.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CommentDto {

  @JsonProperty("id")
  private Long id;
//  @JsonProperty("name")
//  @JsonIgnore
//  private String name;
//  @JsonProperty("email")
//  @JsonIgnore
//  private String email;
  @JsonProperty("body")
  private String body;

  public CommentDto(Comment comment) {
    this.id = comment.getId();
//    this.name = comment.getName();
//    this.email = comment.getEmail();
    this.body = comment.getBody();
  }
}
//public record CommentDto(
//  @JsonProperty("id") Long id,
//  @JsonProperty("name") String name,
//  @JsonProperty("email") String email,
//  @JsonProperty("body") String body
//) {}
