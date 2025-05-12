package io.gitgub.gabrielsizilio.lunardocs.dto.authenticationDto;

public record RegisterUserRequestDTO(String firstName, String lastName, String email, String password, String role) {
}
