package br.com.compasso.posthistoryreactiveapi.entity;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class History {

  @Id
  private String id;
  private Instant date;
  private Status status;
  private Long postId;

  public History(Status status, Long postId) {
    this.status = status;
    this.postId = postId;
    this.date = Instant.now();
  }

}
