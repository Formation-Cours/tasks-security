package com.formation.tasksecurity.controllers;

import com.formation.tasksecurity.dtos.AuthenticationDTO;
import com.formation.tasksecurity.entities.UserEntity;
import com.formation.tasksecurity.enums.TypeRoleEnum;
import com.formation.tasksecurity.jwt.JwtConfig;
import com.formation.tasksecurity.services.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class AuthController {

    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtConfig jwtConfig;

    public AuthController(AuthService authService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return authService.register(userEntity).isPresent()
                ? ResponseEntity.ok(Map.of("message", "L'enregistrement s'est bien passé"))
                : ResponseEntity.badRequest().body(Map.of("message", "L'enregistrement a échoué"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );
        if (authentication.isAuthenticated()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtConfig.generateToken(dto.email()).get("Bearer"));

            Map<String, String> body = Map.of("message", "L'authentification a réussi");

            return new ResponseEntity<>(body, headers, HttpStatus.OK);
            // Version body
            // return ResponseEntity.ok(jwtConfig.generateToken(dto.email()));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "L'authentification a échoué"));
    }
}
