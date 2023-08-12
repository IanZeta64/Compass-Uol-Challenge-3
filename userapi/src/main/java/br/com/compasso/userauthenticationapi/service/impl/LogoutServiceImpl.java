package br.com.compasso.userauthenticationapi.service.impl;

import br.com.compasso.userauthenticationapi.entity.TokenValidator;
import br.com.compasso.userauthenticationapi.entity.User;
import br.com.compasso.userauthenticationapi.repository.TokenRepository;
import br.com.compasso.userauthenticationapi.repository.UserRepository;
import br.com.compasso.userauthenticationapi.security.jwt.JwtService;
import br.com.compasso.userauthenticationapi.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(email).orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));
        var authentication = new UsernamePasswordAuthenticationToken(email, user.getPassword());
        logout(request, response, authentication);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }
        final String jwt = header.substring(7);
        var storedToken = tokenRepository.findByToken(jwt);
        storedToken.ifPresent(tokenValidator -> tokenValidator.setExpired(true));
        Long userId =storedToken.orElseThrow().getUserId();
        List<TokenValidator> tokenValidatorList = tokenRepository.findAll().stream().filter(tokenValidator ->
                tokenValidator.getUserId().equals(userId)).toList();
        if (!tokenValidatorList.isEmpty()){
            List<TokenValidator> tokenValidatorListExpired = tokenValidatorList.stream().peek(tokenFromList -> tokenFromList.setExpired(true)).toList();
            tokenRepository.saveAll(tokenValidatorListExpired);
        }
        tokenRepository.save(storedToken.get());



    }
}
