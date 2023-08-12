package br.com.compasso.posthistoryapi.manager.states;


import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.manager.PostManager;

public interface PostState {


  History handleState(PostManager postManager);

  History handleDisable(PostManager postManager);
}
