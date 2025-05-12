package io.gitgub.gabrielsizilio.lunardocs.dto.credentialDto;

import java.util.UUID;

public record CredentialRequestDTO(String email, String password, UUID userId) {
}
