package br.com.compasso.posthistoryapi.controller.impl;

import br.com.compasso.posthistoryapi.controller.PostController;
import br.com.compasso.posthistoryapi.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

  private final PostService service;
  @Override
  public ResponseEntity<Void> process(Long id) {
    service.save(id);
    return ResponseEntity.ok(null);
  }

  @Override
  public ResponseEntity<Void> disable(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Void> reprocess(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<List<Object>> findAll() {
    return null;
  }
}
