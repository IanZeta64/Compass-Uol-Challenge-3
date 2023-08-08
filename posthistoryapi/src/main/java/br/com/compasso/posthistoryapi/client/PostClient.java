package br.com.compasso.posthistoryapi.client;
import br.com.compasso.posthistoryapi.client.requests.CommentDtoRequest;
import br.com.compasso.posthistoryapi.client.requests.PostDtoRequest;
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
  Optional<PostDtoRequest> findPostById(@PathVariable("postId") Long postId);
  @GetMapping("/{postId}/comments")
  Optional<List<CommentDtoRequest>> findCommentByPostId(@PathVariable("postId") Long postId);
}
