package br.com.compasso.posthistoryapi.entity;
import br.com.compasso.posthistoryapi.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
//@SequenceGenerator(name = "history_seq", sequenceName = "history_seq", allocationSize = 1)
@Getter
@Setter
@ToString
@JsonIgnoreProperties("post")
public class History {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY
//    , generator = "history_seq"
  )
  private Long id;

  private Instant date;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "post_id")
  Long postId;

  public History(Status status, Long postId) {
    this.status = status;
    this.postId = postId;
    this.date = Instant.now();
  }
}
