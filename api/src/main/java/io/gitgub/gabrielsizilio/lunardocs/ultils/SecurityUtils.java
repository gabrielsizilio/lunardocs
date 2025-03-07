package io.gitgub.gabrielsizilio.lunardocs.ultils;

import io.gitgub.gabrielsizilio.lunardocs.domain.credential.Credential;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {
    public static UUID getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Credential) {
            return ((Credential) principal).getUser().getId();
        } else {
            return null;
        }
    }
}
