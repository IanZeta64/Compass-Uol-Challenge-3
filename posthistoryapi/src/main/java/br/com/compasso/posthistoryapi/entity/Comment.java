package br.com.compasso.posthistoryapi.entity;
import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment implements Serializable {

//  @Serial
//  @JsonIgnore
//  @Transient
//  private static final long serialVersionUID = GlobalConstants.COMMENT_serialVersionUID;

  @Id
  private Long id;
  @Column(length = 500)
  private String body;
  @Column(name = "post_id")
  Long postId;

  public Comment(CommentDto commentDto, Long postId) {
    this.id = commentDto.getId();
    this.body = commentDto.getBody();
    this.postId = postId;
  }
}
