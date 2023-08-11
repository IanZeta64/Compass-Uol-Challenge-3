package br.com.compasso.posthistoryapi.entity;

import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties({"histories", "comments"})
public class Post implements Serializable {
//  @Serial
//  @JsonIgnore
//  @Transient
//  private static final long serialVersionUID = GlobalConstants.POST_serialVersionUID;
  @Id
  private Long id;
  private String title;
  private String body;
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "post_id", referencedColumnName = "id")
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "post_id", referencedColumnName = "id")
  private List<History> histories = new ArrayList<>();

  public Post(Long id) {
    this.id = id;
    this.comments = new ArrayList<>();
    this.histories = new ArrayList<>();
  }

//  public Post(Long id, List<History> histories) {
//    this.id = id;
//    this.comments = new ArrayList<>();
//    this.histories = histories;
//  }

  public Post(PostDto postDto, List<History> histories) {
    this.id = postDto.getId();
    this.title = postDto.getTitle();
    this.body = postDto.getBody();
    this.comments = new ArrayList<>();
    this.histories = histories;
  }
}
