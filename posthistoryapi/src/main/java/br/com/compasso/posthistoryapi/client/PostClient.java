package br.com.compasso.posthistoryapi.client;

import br.com.compasso.posthistoryapi.client.dto.CommentDto;
import br.com.compasso.posthistoryapi.client.dto.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import static br.com.compasso.posthistoryapi.constants.GlobalConstants.EXTERNAL_API_CLIENT;


@Component
@FeignClient(value = "PostClient", url = EXTERNAL_API_CLIENT)
public interface PostClient {

  @GetMapping("/{postId}")
  PostDto findPostById(@PathVariable("postId") Long postId);

  @GetMapping("/{postId}/comments")
  List<CommentDto> findCommentByPostId(@PathVariable("postId") Long postId);
}
