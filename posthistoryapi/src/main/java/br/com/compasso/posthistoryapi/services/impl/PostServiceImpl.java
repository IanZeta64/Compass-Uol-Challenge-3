package br.com.compasso.posthistoryapi.services.impl;
import br.com.compasso.posthistoryapi.client.PostClient;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.enums.Status;
import br.com.compasso.posthistoryapi.repositories.HistoryRepository;
import br.com.compasso.posthistoryapi.responses.PostResponse;
import br.com.compasso.posthistoryapi.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostClient client;
  private final HistoryRepository historyRepository;

  @Override
  public void save(Long postId) {
    if (historyRepository.existsByPostId(postId)) {
        throw new RuntimeException();
    }
    PostResponse response = setCreated(postId);
    System.out.println(response);
    response.addHistory(historyRepository.save(new History(Status.POST_FIND, postId)));
    response = postFind(postId, response);
    System.out.println(response);
    response.addHistory(historyRepository.save(new History(Status.COMMENTS_FIND, postId)));
    response = commentFind(postId, response);
    System.out.println(response);
    response = setEnabled(postId, response);
    System.out.println(response);
  }

  private PostResponse setCreated(Long postId) {
    History historyCreated = historyRepository.save(new History(Status.CREATED, postId));
    return new PostResponse(historyCreated);
  }

  private PostResponse setEnabled(Long postId, PostResponse response) {
    if(!response.getHistories().contains(Status.POST_OK) && response.getHistories().contains(Status.COMMENTS_OK)){
      throw new RuntimeException();
    }
    History history = historyRepository.save(new History(Status.ENABLED, postId));
    response.addHistory(history);
    return response;
  }

  private PostResponse commentFind(Long postId, PostResponse postResponse) {
   return client.findCommentByPostId(postId)
     .map(comment -> {
       postResponse.addComment(comment);
       postResponse.addHistory(historyRepository.save(new History(Status.COMMENTS_OK, postId)));
       return  postResponse;
     }).orElseGet(() ->  {
       postResponse.addHistory(historyRepository.save(new History(Status.FAILED, postId)));
       throw new RuntimeException();
     });
  }

  private PostResponse postFind(Long postId, PostResponse postResponse) {
    return client.findPostById(postId)
      .map(post -> {
        postResponse.setPost(post);
        postResponse.addHistory(historyRepository.save(new History(Status.POST_OK, postId)));
        return  postResponse;
      }).orElseGet(() ->  {
        postResponse.addHistory(historyRepository.save(new History(Status.FAILED, postId)));
        throw new RuntimeException();
      });
  }

  @Override
  public void disable(Long id) {

  }

  @Override
  public void reprocess(Long id) {

  }

  @Override
  public void findAll() {

  }
}
