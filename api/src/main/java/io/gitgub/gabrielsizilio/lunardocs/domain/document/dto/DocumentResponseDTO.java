package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;

import java.util.UUID;

public record DocumentResponseDTO(UserResponseDTO owner, String fileName, String fileDescription, String status) {
}
