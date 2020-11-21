package com.github.xeliz.mail2.repositories;

import com.github.xeliz.mail2.entities.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = AccessToken.class, idClass = Long.class)
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    boolean existsByToken(String address);

    Optional<AccessToken> findByToken(String token);
}
