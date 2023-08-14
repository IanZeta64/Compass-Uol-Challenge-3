package br.com.compasso.userauthenticationapi.controller.impl;

import br.com.compasso.userauthenticationapi.controller.UserController;
import br.com.compasso.userauthenticationapi.dto.UserDtoRequest;
import br.com.compasso.userauthenticationapi.service.UserService;
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
