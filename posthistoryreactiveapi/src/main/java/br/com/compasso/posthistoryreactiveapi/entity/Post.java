package br.com.compasso.posthistoryreactiveapi.entity;import br.com.compasso.posthistoryreactiveapi.client.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.List;

@Document

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {

  @Id
  private String id;
//  @Indexed(unique = true)
  private Long postId;
  private String title;
  private String body;

  @Field("comments")
  private List<Comment> comments = new ArrayList<>();

  @Field("histories")
  private List<History> histories = new ArrayList<>();

  public Post(Long postId) {
    this.postId = postId;
    this.comments = new ArrayList<>();
    this.histories = new ArrayList<>();
  }
  public Post(Long postId, List<History> histories) {
    this.postId = postId;
    this.comments = new ArrayList<>();
    this.histories = histories;
  }

  public Post(PostDto postDto, List<History> histories) {
    this.postId = postDto.postId();
    this.title = postDto.title();
    this.body = postDto.body();
    this.comments = new ArrayList<>();
    this.histories = histories;
  }
}
