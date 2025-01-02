package io.gitgub.gabrielsizilio.lunardocs.repository;

import io.gitgub.gabrielsizilio.lunardocs.domain.credential.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserDetailRepository extends JpaRepository<Credential, UUID> {

    Optional<Credential> findByEmail(String email);
}
