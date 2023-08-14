package br.com.compasso.posthistoryreactiveapi.controller;

import br.com.compasso.posthistoryreactiveapi.response.PostResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RequestMapping("/posts")
public interface PostController {


    @PostMapping("/{id}")
    Mono<ResponseEntity<Mono<Void>>> process(@PathVariable
                                 @Min(value = 1, message = "post id must be at least 1")
                                 @Max(value = 100, message = "post id must be at most 100")
                                 Long id);

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Mono<Void>>> disable(@PathVariable
                                 @Min(value = 1, message = "post id must be at least 1")
                                 @Max(value = 100, message = "post id must be at most 100")
                                 Long id);

    @PutMapping("/{id}")
    Mono<ResponseEntity<Mono<Void>>> reprocess(@PathVariable
                                   @Min(value = 1, message = "post id must be at least 1")
                                   @Max(value = 100, message = "post id must be at most 100")
                                   Long id);

    @GetMapping
    Mono<ResponseEntity<Flux<PostResponse>>> findAll();

}
