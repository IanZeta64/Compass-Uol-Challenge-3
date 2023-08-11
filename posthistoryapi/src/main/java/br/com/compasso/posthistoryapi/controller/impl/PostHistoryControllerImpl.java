package br.com.compasso.posthistoryapi.controller.impl;

import br.com.compasso.posthistoryapi.controller.PostHistoryController;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequiredArgsConstructor
public class PostHistoryControllerImpl implements PostHistoryController {

  private final PostHistoryService service;
  @Override
  public ResponseEntity<Void> process(Long id) throws InterruptedException {
    service.process(id);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Void> disable(Long id) {
    service.disable(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> reprocess(Long id) {
    service.reprocess(id);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<List<PostDtoResponse>> findAll() {
    return ResponseEntity.status(200).body(service.findAll());
  }
}
