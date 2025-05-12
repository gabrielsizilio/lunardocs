package io.gitgub.gabrielsizilio.lunardocs.dto.documentDto;

import org.springframework.web.multipart.MultipartFile;

public record DocumentDTO(MultipartFile file, DocumentRequestDTO documentRequestDTO) {
}
