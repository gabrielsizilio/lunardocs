package io.gitgub.gabrielsizilio.lunardocs.dto.documentDto;

import io.gitgub.gabrielsizilio.lunardocs.dto.userDto.UserDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentSignerDTO(UUID id, UserDTO user, LocalDateTime assignedAt, LocalDateTime signedAt, String documentHash) {
}
