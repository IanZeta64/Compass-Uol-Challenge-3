package br.com.compasso.posthistoryapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


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

