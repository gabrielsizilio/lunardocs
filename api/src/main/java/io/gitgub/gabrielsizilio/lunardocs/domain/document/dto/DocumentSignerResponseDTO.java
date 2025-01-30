package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DocumentSignerResponseDTO(List<DocumentSignerDTO> signers) {
}
