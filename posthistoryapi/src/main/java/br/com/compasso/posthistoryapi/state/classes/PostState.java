package br.com.compasso.posthistoryapi.state.classes;
import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.state.PostStateManager;
import org.springframework.scheduling.annotation.Async;

public interface PostState {

  @Async
  History handleState(PostStateManager postStateManager);
  @Async
  History handleDisabled(PostStateManager postStateManager);
}
