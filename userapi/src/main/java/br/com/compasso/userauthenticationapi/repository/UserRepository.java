package br.com.compasso.userauthenticationapi.repository;

import br.com.compasso.userauthenticationapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}