package br.com.compasso.posthistoryreactiveapi.entity;
import br.com.compasso.posthistoryreactiveapi.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class History {

  @Id
  private Long id;

  private Instant date;

  private Status status;

  @Column("post_id")
  private Long postId;

  public History(Status status, Long postId) {
    this.status = status;
    this.postId = postId;
    this.date = Instant.now();
  }
}
