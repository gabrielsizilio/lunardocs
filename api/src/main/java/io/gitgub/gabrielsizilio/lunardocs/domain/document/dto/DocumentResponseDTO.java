package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DocumentResponseDTO(UUID id, UserResponseDTO owner, String fileName, String fileDescription, String status, List<UUID> signers) {
}
