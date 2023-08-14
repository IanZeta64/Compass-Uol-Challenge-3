package br.com.compasso.posthistoryapi.controller;

import br.com.compasso.posthistoryapi.dto.AuthRequest;
import br.com.compasso.posthistoryapi.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public interface AuthenicationController {

    @PostMapping("/login")
    AuthResponse login(@RequestBody AuthRequest authRequest);

    @PostMapping("/logout")
    void logout(HttpServletRequest request, HttpServletResponse response);
}
