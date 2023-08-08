package br.com.compasso.posthistoryapi.entity;
import br.com.compasso.posthistoryapi.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;


@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "history_seq", sequenceName = "history_seq", allocationSize = 1)
@ToString
public class History {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_seq")
  private Long id;
  private Long postId;
  private Instant date;
  @Enumerated(EnumType.STRING)
  private Status status;

  public History(Status status, Long postId) {
    this.status = status;
    this.postId = postId;
    this.date = Instant.now();
  }
}
