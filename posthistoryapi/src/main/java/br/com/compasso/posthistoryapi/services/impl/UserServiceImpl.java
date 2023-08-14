package br.com.compasso.posthistoryapi.services.impl;
import br.com.compasso.posthistoryapi.dto.UserDtoRequest;
import br.com.compasso.posthistoryapi.entity.User;
import br.com.compasso.posthistoryapi.enums.Role;
import br.com.compasso.posthistoryapi.exceptions.exceptionclass.DuplicateUserException;
import br.com.compasso.posthistoryapi.repositories.UserRepository;
import br.com.compasso.posthistoryapi.services.UserService;
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
        repository.save(new User(userDtoRequest.username(),pwdEncoded, Role.valueOf(userDtoRequest.role())));
    }
}
