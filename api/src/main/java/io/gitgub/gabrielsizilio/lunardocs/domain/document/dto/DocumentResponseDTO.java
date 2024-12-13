package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;

import java.util.UUID;

public record DocumentResponseDTO(UUID id, UserResponseDTO owner, String fileName, String fileDescription, String status) {
}
