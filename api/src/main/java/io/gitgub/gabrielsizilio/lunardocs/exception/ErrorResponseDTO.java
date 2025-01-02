package io.gitgub.gabrielsizilio.lunardocs.exception;

public record ErrorResponseDTO(Integer code, String message, String details) {
}
