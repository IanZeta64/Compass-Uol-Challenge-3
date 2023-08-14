package br.com.compasso.posthistoryapi.services;

import br.com.compasso.posthistoryapi.dto.AuthRequest;
import br.com.compasso.posthistoryapi.dto.AuthResponse;

public interface AuthenticationService {

    AuthResponse login(AuthRequest authRequest);

}
