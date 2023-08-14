package br.com.compasso.posthistoryapi.entity;
import br.com.compasso.posthistoryapi.client.dto.CommentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment  {


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
