package br.com.compasso.posthistoryapi.manager.states;


import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.manager.PostManager;

public interface PostState {

  void handleState(PostManager post, History history);

  void handleDisabled(PostManager post, History history);
}
