package io.gitgub.gabrielsizilio.lunardocs.dto.userDto;

import java.util.UUID;

public record UserResponseDTO(UUID id, String firstName, String lastName, String email, String cpf, String role) {
}

