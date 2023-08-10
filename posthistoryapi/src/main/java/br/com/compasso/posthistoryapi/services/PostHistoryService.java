package br.com.compasso.posthistoryapi.services;

import br.com.compasso.posthistoryapi.dto.PostDtoResponse;

import java.util.List;

public interface PostHistoryService {

  PostDtoResponse process(Long id);
  PostDtoResponse disable(Long id);
  PostDtoResponse reprocess(Long id);
  List<PostDtoResponse> findAll();
}
