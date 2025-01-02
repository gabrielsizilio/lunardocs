package io.gitgub.gabrielsizilio.lunardocs.repository;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
