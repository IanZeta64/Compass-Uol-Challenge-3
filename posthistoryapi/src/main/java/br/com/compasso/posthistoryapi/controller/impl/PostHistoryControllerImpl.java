package br.com.compasso.posthistoryapi.controller.impl;

import br.com.compasso.posthistoryapi.controller.PostHistoryController;
import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import br.com.compasso.posthistoryapi.services.PostHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostHistoryControllerImpl implements PostHistoryController {

  private final PostHistoryService service;
  @Override
  public ResponseEntity<Void> process(Long id) throws InterruptedException {
    log.info("PROCESS - request received for post -{}", id);
    service.process(id);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Void> disable(Long id) {
    log.info("DISABLE - request received for post -{}", id);
    service.disable(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> reprocess(Long id) {
    log.info("REPROCESS - request received for post -{}", id);
    service.reprocess(id);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<List<PostDtoResponse>> findAll() {
    log.info("QUERY - request received for all post");
    return ResponseEntity.status(200).body(service.findAll());
  }
}
