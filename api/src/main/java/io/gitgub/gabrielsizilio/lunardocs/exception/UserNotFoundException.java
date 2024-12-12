package io.gitgub.gabrielsizilio.lunardocs.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
