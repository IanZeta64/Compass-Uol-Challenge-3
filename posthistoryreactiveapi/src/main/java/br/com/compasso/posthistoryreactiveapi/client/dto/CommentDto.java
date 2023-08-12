package br.com.compasso.posthistoryreactiveapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentDto(@JsonProperty("id")
                         Long id,
                         @JsonProperty("body")
                         String body) {
}
