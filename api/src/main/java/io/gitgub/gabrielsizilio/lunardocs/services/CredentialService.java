package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.domain.credential.Credential;
import io.gitgub.gabrielsizilio.lunardocs.domain.credential.dto.CredentialRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.credential.dto.CredentialResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.exception.customException.ConflictException;
import io.gitgub.gabrielsizilio.lunardocs.exception.customException.UserNotFoundException;
import io.gitgub.gabrielsizilio.lunardocs.repository.CredentialRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public CredentialService(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    //CREATE
    @Transactional
    public CredentialResponseDTO createCredential(CredentialRequestDTO data) {
        Optional<Credential> credentialOptional = credentialRepository.findByEmail(data.email());
        if (credentialOptional.isPresent()) {
            throw new ConflictException("User already exists with email: " + data.email());
        }

        User user = userService.findUserById(data.userId());
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + data.email());
        }

        //TODO: criar lógica para o token de reset

        Credential credential = new Credential(user, data.email(), data.password());
        Credential credentialSaved = credentialRepository.save(credential);
        return convertToResponseDTO(credentialSaved);
    }

    //READ
    @Transactional
    public Credential findCredentialByEmail(String email) {
        Optional<Credential> credentialOptional = credentialRepository.findByEmail(email);
        if (credentialOptional.isPresent()) {
            return credentialOptional.get();
        } else {
            throw new UserNotFoundException("Credential not found with email: " + email);
        }
    }

    //UPDATE
    @Transactional
    public CredentialResponseDTO updateCredential(String id, CredentialRequestDTO data) {
        Optional<Credential> credentialOptional = credentialRepository.findById(UUID.fromString(id));

        if (credentialOptional.isPresent()) {
            Credential credential = credentialOptional.get();
            credential.setEmail(data.email());

            if(data.password() != null || !data.password().isEmpty()) {
                String passwordEncrypted = passwordEncoder.encode(data.password());
                credential.setPassword(passwordEncrypted);
            }

            //TODO: criar lógica para token de reset
            //credential.setResetToken();

            credentialRepository.save(credential);
            return convertToResponseDTO(credential);
        } else {
            throw new UserNotFoundException("Credential not found with email: " + data.email());
        }
    }


    //DELETE
    @Transactional
    public Boolean deleteCredential(String id) {
        UUID uuid = UUID.fromString(id);
        if(credentialRepository.existsById(uuid)) {
            credentialRepository.deleteById(uuid);
            return true;
        } else {
            throw new UserNotFoundException("Credential not found with id: " + id);
        }
    }

    private CredentialResponseDTO convertToResponseDTO(Credential credential) {
        return new CredentialResponseDTO(credential.getId(), credential.getEmail());
    }
}
