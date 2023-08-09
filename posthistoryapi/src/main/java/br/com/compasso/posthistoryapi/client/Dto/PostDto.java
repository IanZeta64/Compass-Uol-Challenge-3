package br.com.compasso.posthistoryapi.client.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PostDto {
  @JsonProperty("id")
  private Long id;
  @JsonProperty("title")
  private String title;
  @JsonProperty("body")
  private String body;
}
//public record PostDto(
//  @JsonProperty("id") Long id,
//  @JsonProperty("title") String title,
//  @JsonProperty("body") String body
//) {}
