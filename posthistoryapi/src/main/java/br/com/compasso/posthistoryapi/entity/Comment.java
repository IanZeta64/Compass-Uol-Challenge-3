package br.com.compasso.posthistoryapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties("post")
public class Comment {

  @Id
  private Long id;
//  private String name;
//  private String email;
  @Column(length = 500)
  private String body;
  @Column(name = "post_id")
  Long postId;

}
