package io.gitgub.gabrielsizilio.lunardocs.domain.credential.dto;

import java.util.UUID;

public record CredentialRequestDTO(String email, String password, UUID userId) {
}
