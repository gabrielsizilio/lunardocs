package io.gitgub.gabrielsizilio.lunardocs.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService  implements UserDetailsService {

    private final UserDetailService userDetailService;

    public AuthorizationService(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDetails userDetails = userDetailService.findByEmail(email);
        if(userDetails == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        } else {
            return userDetails;
        }
    }
}
