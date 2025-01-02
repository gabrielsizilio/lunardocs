package io.gitgub.gabrielsizilio.lunardocs.controllers.authentication.dto;

public record RegisterUserRequestDTO(String firstName, String lastName, String email, String password, String role) {
}
