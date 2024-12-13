package io.gitgub.gabrielsizilio.lunardocs.ultils;

import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public class SecurityUtils {
    public static UUID getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getId();
        } else {
            return null;
        }
    }
}
