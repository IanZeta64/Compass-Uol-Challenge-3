package br.com.compasso.posthistoryapi.client.Dto;

import br.com.compasso.posthistoryapi.constants.GlobalConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PostDto  {

  @JsonProperty("id")
  private Long id;
  @JsonProperty("title")
  private String title;
  @JsonProperty("body")
  private String body;
}

