package br.com.compasso.posthistoryapi.repositories;

import br.com.compasso.posthistoryapi.entity.TokenValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenValidator, Long> {
    Optional<TokenValidator> findByToken(String jwt);
    Optional<TokenValidator> findAllByUserId(Long userId);
}
