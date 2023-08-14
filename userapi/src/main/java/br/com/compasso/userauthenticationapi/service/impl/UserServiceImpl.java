package br.com.compasso.userauthenticationapi.service.impl;

import br.com.compasso.userauthenticationapi.dto.UserDtoRequest;
import br.com.compasso.userauthenticationapi.entity.User;
import br.com.compasso.userauthenticationapi.exceptions.DuplicateUserException;
import br.com.compasso.userauthenticationapi.repository.UserRepository;
import br.com.compasso.userauthenticationapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(UserDtoRequest userDtoRequest) {
        if(Boolean.TRUE.equals(repository.existsByUsername(userDtoRequest.username()))){
            throw new DuplicateUserException("");
        }
        String pwdEncoded = passwordEncoder.encode(userDtoRequest.password());
        repository.save(new User(userDtoRequest.username(),pwdEncoded));
    }
}
