package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.domain.credential.dto.CredentialRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.credential.dto.CredentialResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialService {

    private final UserService userService;
    private final CredentialService credentialService;

    public UserCredentialService(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    //CREATE
    @Transactional
    public UserResponseDTO createUserWithCredential (UserRequestDTO data) {
        UserResponseDTO user = userService.save(data);
        CredentialResponseDTO credential = credentialService.createCredential(new CredentialRequestDTO(user.email(), data.password(), user.id()));
        System.out.println(">> User created successfully: " + user.email());
        return user;
    }


}
