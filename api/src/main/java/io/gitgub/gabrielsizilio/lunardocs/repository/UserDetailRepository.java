package io.gitgub.gabrielsizilio.lunardocs.repository;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);
}
