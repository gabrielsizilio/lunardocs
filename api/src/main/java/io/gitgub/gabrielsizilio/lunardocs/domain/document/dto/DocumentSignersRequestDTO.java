package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import java.util.List;
import java.util.UUID;

    public record DocumentSignersRequestDTO(List<UUID> signersIds) {
}
