package br.com.compasso.posthistoryapi.client.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PostDtoRequest {
  @JsonProperty("id")
  private Long id;
  @JsonProperty("title")
  private String title;
  @JsonProperty("body")
  private String body;
}
