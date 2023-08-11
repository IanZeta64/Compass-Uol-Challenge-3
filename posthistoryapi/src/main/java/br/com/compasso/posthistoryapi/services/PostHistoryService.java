package br.com.compasso.posthistoryapi.services;

import br.com.compasso.posthistoryapi.dto.PostDtoResponse;

import java.util.List;

public interface PostHistoryService {

  void process(Long id) throws InterruptedException;
  void disable(Long id);
  void reprocess(Long id);
  List<PostDtoResponse> findAll();
}
