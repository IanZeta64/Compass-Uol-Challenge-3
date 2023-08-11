package br.com.compasso.posthistoryapi.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment implements Serializable {

  @Id
  private Long id;
  @Column(length = 500)
  private String body;
  @Column(name = "post_id")
  Long postId;

}
