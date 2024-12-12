package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import java.util.UUID;

public record DocumentResponseDTO(UUID id, String ownerName, String name, String description, String url) {
}
