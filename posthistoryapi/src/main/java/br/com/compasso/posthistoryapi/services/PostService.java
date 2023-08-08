package br.com.compasso.posthistoryapi.services;

public interface PostService {

  void save(Long id);
  void disable(Long id);
  void reprocess(Long id);
  void findAll();
}
