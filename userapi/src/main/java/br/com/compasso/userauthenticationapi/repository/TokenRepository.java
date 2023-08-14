package br.com.compasso.userauthenticationapi.repository;
;
import br.com.compasso.userauthenticationapi.entity.TokenValidator;
import ch.qos.logback.core.subst.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenValidator, Long> {
    Optional<TokenValidator> findByToken(String jwt);
    Optional<TokenValidator> findAllByUserId(Long userId);
}
