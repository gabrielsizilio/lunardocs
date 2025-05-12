package io.gitgub.gabrielsizilio.lunardocs.dto.documentDto;

import org.springframework.core.io.FileSystemResource;

public record DocumentResponseDownloadDTO(String fileName, FileSystemResource fileSystemResource) {
}
