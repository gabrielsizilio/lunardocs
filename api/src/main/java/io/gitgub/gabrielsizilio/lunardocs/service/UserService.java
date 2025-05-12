package io.gitgub.gabrielsizilio.lunardocs.service;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.UserRole;
import io.gitgub.gabrielsizilio.lunardocs.dto.userDto.UserRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.dto.userDto.UserResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.exception.customException.ConflictException;
import io.gitgub.gabrielsizilio.lunardocs.exception.customException.UserNotFoundException;
import io.gitgub.gabrielsizilio.lunardocs.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //CREATE
    @Transactional
    public UserResponseDTO save(UserRequestDTO data) {
        Optional<User> userOptional = userRepository.findByEmail(data.email());
        if(userOptional.isPresent()) {
            throw new ConflictException("User already exists");
        }

        User user = new User(data.firstName(),
                data.lastName(),
                data.email(),
                data.cpf(),
                UserRole.valueOf(data.role()));
        user = userRepository.save(user);

        return convertToResponseDTO(user);
    }

    //READ
    @Transactional
    public UserResponseDTO findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            return convertToResponseDTO(userOptional.get());
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    @Transactional
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public User findUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    //UPDATE
    @Transactional
    public UserResponseDTO updateUser(String id, UserRequestDTO data) {
        Optional<User> userOptional = userRepository.findById(UUID.fromString(id));
        //Credential credential = credentialService.findCredentialByEmail(data.email());

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(data.firstName());
            user.setLastName(data.lastName());
            user.setEmail(data.email());
            user.setRole(UserRole.valueOf(data.role()));

            user = userRepository.save(user);

          //  credentialService.updateCredential(String.valueOf(credential.getId()), new CredentialRequestDTO(user.getEmail(), data.password(), user.getId()));

            return convertToResponseDTO(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    //DELETE
    @Transactional
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if(userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCpf(),
                user.getRole().toString());
    }
}
