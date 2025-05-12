package io.gitgub.gabrielsizilio.lunardocs.service;

import io.gitgub.gabrielsizilio.lunardocs.dto.credentialDto.CredentialRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.dto.credentialDto.CredentialResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.dto.userDto.UserRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.dto.userDto.UserResponseDTO;
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
