package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import io.gitgub.gabrielsizilio.lunardocs.domain.document.StatusDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record DocumentRequestDTO(String name, String description, String statusDocument) {
}
