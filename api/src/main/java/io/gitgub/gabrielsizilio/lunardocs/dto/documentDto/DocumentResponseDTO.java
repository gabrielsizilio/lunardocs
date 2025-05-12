package io.gitgub.gabrielsizilio.lunardocs.dto.documentDto;

import io.gitgub.gabrielsizilio.lunardocs.dto.userDto.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public record DocumentResponseDTO(UUID id, UserResponseDTO owner, String fileName, String fileDescription, String status, List<UUID> signers) {
}
