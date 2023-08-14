package br.com.compasso.posthistoryreactiveapi.entity;import br.com.compasso.posthistoryreactiveapi.client.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.util.ArrayList;
import java.util.List;

@Table("post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Post {

  @Id
  private Long id;

  private String title;
  private String body;

  @Column("post_id")
  private List<Comment> comments = new ArrayList<>();
  @Column("post_id")
  private List<History> histories = new ArrayList<>();

  public Post(Long id) {
    this.id = id;
    this.comments = new ArrayList<>();
    this.histories = new ArrayList<>();
  }

  public Post(PostDto postDto, List<History> histories) {
    this.id = postDto.id();
    this.title = postDto.title();
    this.body = postDto.body();
    this.comments = new ArrayList<>();
    this.histories = histories;
  }
}
