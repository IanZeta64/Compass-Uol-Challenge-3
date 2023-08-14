package br.com.compasso.posthistoryreactiveapi.manager.states;


import br.com.compasso.posthistoryreactiveapi.entity.History;
import br.com.compasso.posthistoryreactiveapi.manager.PostManager;

import java.io.Serializable;

public interface PostState extends Serializable {


  void handleState(PostManager post, History history);

  void handleDisabled(PostManager post, History history);
}
