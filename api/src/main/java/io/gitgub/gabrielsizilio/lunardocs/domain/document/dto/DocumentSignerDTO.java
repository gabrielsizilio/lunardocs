package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentSignerDTO(UUID id, UserDTO user, LocalDateTime assignedAt, LocalDateTime signedAt, String documentHash) {
}
