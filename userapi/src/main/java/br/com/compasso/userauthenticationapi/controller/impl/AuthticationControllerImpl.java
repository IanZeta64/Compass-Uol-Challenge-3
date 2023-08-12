package br.com.compasso.userauthenticationapi.controller.impl;

import br.com.compasso.userauthenticationapi.controller.AuthenicationController;
import br.com.compasso.userauthenticationapi.dto.AuthRequest;
import br.com.compasso.userauthenticationapi.dto.AuthResponse;
import br.com.compasso.userauthenticationapi.service.AuthenticationService;
import br.com.compasso.userauthenticationapi.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthticationControllerImpl implements AuthenicationController {
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;


    @Override
    public AuthResponse login(AuthRequest authRequest) {
        return authenticationService.login(authRequest);
    }

    @Override
    public AuthResponse logout(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
