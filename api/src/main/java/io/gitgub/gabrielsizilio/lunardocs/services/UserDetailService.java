package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.exception.customException.UserNotFoundException;
import io.gitgub.gabrielsizilio.lunardocs.repository.UserDetailRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;

    public UserDetailService(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    public UserDetails findByEmail(String email) {
        return userDetailRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
    }
}
