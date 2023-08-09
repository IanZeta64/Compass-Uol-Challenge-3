package br.com.compasso.posthistoryapi.dto;
import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.entity.Post;


import java.util.List;

public record PostDtoResponse(
  PostDto post,
  List<CommentDto> comment,
  List<History> history
) {
  public PostDtoResponse(Post post) {
    this(new PostDto(post.getId(), post.getTitle(), post.getBody()),
      post.getComments().parallelStream().map(CommentDto::new).toList(),
      post.getHistories());

  }
}
