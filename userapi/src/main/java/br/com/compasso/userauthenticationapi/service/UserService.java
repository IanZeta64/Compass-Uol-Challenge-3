package br.com.compasso.userauthenticationapi.service;

import br.com.compasso.userauthenticationapi.dto.UserDtoRequest;

public interface UserService {

    public void register(UserDtoRequest userDtoRequest);
}
