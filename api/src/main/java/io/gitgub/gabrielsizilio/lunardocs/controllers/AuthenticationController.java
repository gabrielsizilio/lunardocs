package io.gitgub.gabrielsizilio.lunardocs.controllers;

import io.gitgub.gabrielsizilio.lunardocs.controllers.authentication.dto.AuthenticationRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.controllers.authentication.dto.AuthenticationResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.User;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserRequestDTO;
import io.gitgub.gabrielsizilio.lunardocs.domain.user.dto.UserResponseDTO;
import io.gitgub.gabrielsizilio.lunardocs.infra.security.TokenService;
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
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO data) {
        var UserPassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(UserPassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated UserRequestDTO data) {
        UserResponseDTO userSaved = userService.save(data);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }
}
