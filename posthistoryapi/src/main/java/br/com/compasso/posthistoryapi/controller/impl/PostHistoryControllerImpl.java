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
  public ResponseEntity<PostDtoResponse> process(Long id) {
    PostDtoResponse response = service.process(id);
    return ResponseEntity.status(201).body(response);
  }

  @Override
  public ResponseEntity<Void> disable(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<PostDtoResponse> reprocess(Long id) {
    PostDtoResponse response = service.reprocess(id);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<List<PostDtoResponse>> findAll() {
    return ResponseEntity.status(200).body(service.findAll());
  }
}
