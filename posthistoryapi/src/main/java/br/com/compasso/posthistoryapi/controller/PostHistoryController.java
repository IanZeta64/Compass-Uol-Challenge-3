package br.com.compasso.posthistoryapi.controller;

import br.com.compasso.posthistoryapi.dto.PostDtoResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/posts")
@Validated
public interface PostHistoryController {

  @PostMapping("/{id}")
  ResponseEntity<Void> process(@PathVariable
            @Min(value = 1, message = "post id must be at least 1")
            @Max(value = 100, message = "post id must be at most 100")
                               Long id) throws InterruptedException;

  @DeleteMapping("/{id}")
  ResponseEntity<Void> disable(@PathVariable
                               @Min(value = 1, message = "post id must be at least 1")
                               @Max(value = 100, message = "post id must be at most 100")
                               Long id);

  @PutMapping("/{id}")
  ResponseEntity<Void> reprocess(@PathVariable
                               @Min(value = 1, message = "post id must be at least 1")
                               @Max(value = 100, message = "post id must be at most 100")
                                 Long id);

  @GetMapping
  ResponseEntity<List<PostDtoResponse>> findAll();

}
