package br.com.compasso.posthistoryapi.controller.impl;

import br.com.compasso.posthistoryapi.controller.UserController;
import br.com.compasso.posthistoryapi.dto.UserDtoRequest;
import br.com.compasso.posthistoryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;
    @Override
    public ResponseEntity<Void> register(UserDtoRequest userDtoRequest) {
        userService.register(userDtoRequest);
        return ResponseEntity.ok().build();
    }
}
