package io.gitgub.gabrielsizilio.lunardocs.dto.documentDto;

import java.util.List;
import java.util.UUID;

    public record DocumentSignersRequestDTO(List<UUID> signersIds) {
}
