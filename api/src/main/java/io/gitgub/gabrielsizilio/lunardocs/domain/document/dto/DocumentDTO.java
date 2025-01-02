package io.gitgub.gabrielsizilio.lunardocs.domain.document.dto;

import org.springframework.web.multipart.MultipartFile;

public record DocumentDTO(MultipartFile file, DocumentRequestDTO documentRequestDTO) {
}
