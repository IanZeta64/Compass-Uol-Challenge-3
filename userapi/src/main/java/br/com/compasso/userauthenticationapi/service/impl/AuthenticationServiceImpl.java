package br.com.compasso.userauthenticationapi.service.impl;

import br.com.compasso.userauthenticationapi.dto.AuthRequest;
import br.com.compasso.userauthenticationapi.dto.AuthResponse;
import br.com.compasso.userauthenticationapi.entity.TokenValidator;
import br.com.compasso.userauthenticationapi.entity.User;
import br.com.compasso.userauthenticationapi.repository.TokenRepository;
import br.com.compasso.userauthenticationapi.repository.UserRepository;
import br.com.compasso.userauthenticationapi.security.jwt.JwtService;
import br.com.compasso.userauthenticationapi.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    @Override
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.
                findByUsername(authRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        List<TokenValidator> tokenValidatorList = tokenRepository.findAll().stream()
                .filter(tokenValidator -> tokenValidator.getUserId().equals(user.getId())).toList();
        if (!tokenValidatorList.isEmpty()){
            List<TokenValidator> tokenValidatorListExpired = tokenValidatorList.stream().peek(tokenFromList -> tokenFromList.setExpired(true)).toList();
            tokenRepository.saveAll(tokenValidatorListExpired);
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.username(), authRequest.password());
        authenticationManager.authenticate(authentication);
        String token = jwtService.createToken(user);


        TokenValidator tokenValidator = new TokenValidator();
        tokenValidator.setExpired(false);
        tokenValidator.setUserId(user.getId());
        tokenValidator.setToken(token);
        tokenValidator.setRole(user.getRole());

        tokenRepository.save(tokenValidator);
        return new AuthResponse(user.getId(), token);
    }

}
