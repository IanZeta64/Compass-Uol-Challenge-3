package br.com.compasso.posthistoryapi.manager.states;


import br.com.compasso.posthistoryapi.entity.History;
import br.com.compasso.posthistoryapi.manager.PostManager;

import java.io.Serializable;

public interface PostState extends Serializable {


  void handleState(PostManager post, History history);

  void handleDisabled(PostManager post, History history);
}
