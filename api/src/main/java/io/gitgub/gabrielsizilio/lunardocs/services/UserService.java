package io.gitgub.gabrielsizilio.lunardocs.services;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.Document;
import io.gitgub.gabrielsizilio.lunardocs.domain.document.dto.DocumentRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.UserRole;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.exception.customException.ConflictException;
import io.gitgub.gabrielsizilio.lunardocs.exception.customException.UserNotFoundException;
import io.gitgub.gabrielsizilio.lunardocs.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //CREATE
    public UserResponseDTO save(UserRequestDTO data) {

        Optional<User> userOptional = userRepository.findByEmail(data.email());
        if(userOptional.isPresent()) {
            throw new ConflictException("User already exists");
        }

        User user = new User(data.firstName(),
                data.lastName(),
                data.email(),
                data.password(),
                UserRole.valueOf(data.role()));

        user = userRepository.save(user);
        return convertToResponseDTO(user);
    }

    //READ
    public UserResponseDTO findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            return convertToResponseDTO(userOptional.get());
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public User findUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    //UPDATE
    public UserResponseDTO updateUser(String id, UserRequestDTO data) {
        Optional<User> userOptional = userRepository.findById(UUID.fromString(id));

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(data.firstName());
            user.setLastName(data.lastName());
            user.setEmail(data.email());
            if(data.password() != null || !data.password().isEmpty()) {
                String passwordEncrypted = passwordEncoder.encode(data.password());
                user.setPassword(passwordEncrypted);
            }
            user.setRole(UserRole.valueOf(data.role()));
            user = userRepository.save(user);
            return convertToResponseDTO(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    //DELETE
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
                user.getRole().toString());
    }
}
