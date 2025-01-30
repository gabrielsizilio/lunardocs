package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentSignerDTO(UUID id, User signer, LocalDateTime assignedAt, LocalDateTime signedAt, String documentHash) {
}
