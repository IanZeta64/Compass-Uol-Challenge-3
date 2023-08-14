package br.com.compasso.posthistoryapi.controller.impl;

import br.com.compasso.posthistoryapi.controller.AuthenicationController;
import br.com.compasso.posthistoryapi.dto.AuthRequest;
import br.com.compasso.posthistoryapi.dto.AuthResponse;
import br.com.compasso.posthistoryapi.services.AuthenticationService;
import br.com.compasso.posthistoryapi.services.LogoutService;
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
    public void logout(HttpServletRequest request, HttpServletResponse response) {
         logoutService.logout(request, response);
    }
}
