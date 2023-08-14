package br.com.compasso.posthistoryreactiveapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostDto(@JsonProperty("id")
                      Long postId,
                      @JsonProperty("title") String title,
                      @JsonProperty("body")
                      String body) {

}
