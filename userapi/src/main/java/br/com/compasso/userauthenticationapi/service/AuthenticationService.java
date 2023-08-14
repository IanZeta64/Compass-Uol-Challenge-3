package br.com.compasso.userauthenticationapi.service;

import br.com.compasso.userauthenticationapi.dto.AuthRequest;
import br.com.compasso.userauthenticationapi.dto.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    public AuthResponse login(AuthRequest authRequest);

}
