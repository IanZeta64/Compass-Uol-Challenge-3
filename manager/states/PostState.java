package br.com.compasso.posthistoryapi.statemanager.states;


import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.statemanager.PostManager;

public interface PostState {


  History handleState(PostManager postManager);

  History handleDisable(PostManager postManager);
}
