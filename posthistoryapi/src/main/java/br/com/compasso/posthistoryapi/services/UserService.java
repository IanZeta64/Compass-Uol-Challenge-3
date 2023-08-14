package br.com.compasso.posthistoryapi.services;

import br.com.compasso.posthistoryapi.dto.UserDtoRequest;
public interface UserService {
    void register(UserDtoRequest userDtoRequest);
}
