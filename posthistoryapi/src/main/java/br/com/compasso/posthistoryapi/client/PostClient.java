package br.com.compasso.posthistoryapi.client;
import br.com.compasso.posthistoryapi.client.Dto.CommentDto;
import br.com.compasso.posthistoryapi.client.Dto.PostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;

@Component
@FeignClient(value = "PostClient", url = "https://jsonplaceholder.typicode.com/posts")
public interface PostClient {

  @GetMapping("/{postId}")
  Optional<PostDto> findPostById(@PathVariable("postId") Long postId);

  @GetMapping
  Optional<List<PostDto>> findAllPosts();
  @GetMapping("/{postId}/comments")
  Optional<List<CommentDto>> findCommentByPostId(@PathVariable("postId") Long postId);
}
