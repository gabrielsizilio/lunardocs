package io.gitgub.gabrielsizilio.lunardocs.domain.user.dto;

import java.util.UUID;

public record UserResponseDTO(UUID id, String firstName, String lastName, String email, String role) {
}

