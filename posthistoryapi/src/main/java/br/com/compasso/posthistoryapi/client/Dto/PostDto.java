package br.com.compasso.posthistoryapi.client.Dto;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class PostDto  {

  @JsonProperty("id")
  private Long id;
  @JsonProperty("title")
  private String title;
  @JsonProperty("body")
  private String body;
}

