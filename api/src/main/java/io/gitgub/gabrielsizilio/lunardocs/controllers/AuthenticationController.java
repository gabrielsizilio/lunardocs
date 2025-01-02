package io.gitgub.gabrielsizilio.lunardocs.controllers;

import io.gitgub.gabrielsizilio.lunardocs.controllers.authentication.dto.AuthenticationRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.controllers.authentication.dto.AuthenticationResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.credential.Credential;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.infra.security.TokenService;
import io.gitgub.gabrielsizilio.lunardocs.services.UserCredentialService;
import io.gitgub.gabrielsizilio.lunardocs.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserCredentialService userCredentialService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, UserCredentialService userCredentialService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userCredentialService = userCredentialService;
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO data) {
        var UserPassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(UserPassword);
        var credential = (Credential) auth.getPrincipal();
        var token = tokenService.generateToken(credential.getUser());
        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated UserRequestDTO data) {
        UserResponseDTO userSaved = userCredentialService.createUserWithCredential(data);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }
}
