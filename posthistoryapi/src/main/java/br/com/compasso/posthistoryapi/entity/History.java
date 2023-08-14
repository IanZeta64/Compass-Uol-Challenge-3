package br.com.compasso.posthistoryapi.entity;
import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import br.com.compasso.posthistoryapi.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class History  {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
