package br.com.compasso.userauthenticationapi.controller;

import br.com.compasso.userauthenticationapi.dto.AuthRequest;
import br.com.compasso.userauthenticationapi.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/auth")
public interface AuthenicationController {

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest);

    @PostMapping("/logout")
    public AuthResponse logout(HttpServletRequest request, HttpServletResponse response);
}
