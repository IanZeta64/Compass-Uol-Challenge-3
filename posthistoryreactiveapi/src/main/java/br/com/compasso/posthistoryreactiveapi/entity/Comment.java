package br.com.compasso.posthistoryreactiveapi.entity;

import br.com.compasso.posthistoryreactiveapi.client.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


//@Entity

  @Table(name = "comment")
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  public class Comment {


    @Id
    private Long id;

//    @Column(length = 500)
    private String body;
    @Column(value = "post_id")
    Long postId;

    public Comment(CommentDto commentDto, Long postId) {
      this.id = commentDto.id();
      this.body = commentDto.body();
      this.postId = postId;
    }

}
