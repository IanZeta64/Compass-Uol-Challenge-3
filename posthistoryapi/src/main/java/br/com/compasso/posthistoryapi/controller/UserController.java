package br.com.compasso.posthistoryapi.controller;

import br.com.compasso.posthistoryapi.dto.UserDtoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@Validated
public interface UserController {

    @PostMapping
    ResponseEntity<Void> register(@RequestBody UserDtoRequest userDtoRequest);

}
