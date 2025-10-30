package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.service.AuthenticationService;
import com.peer.tutormatchmaker.dto.RegisterRequest;
import com.peer.tutormatchmaker.dto.LoginRequest;
import com.peer.tutormatchmaker.dto.AuthResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    // Injects the AuthenticationService
    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user registration.
     * Maps to POST /api/v1/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Calls the service layer to handle registration and automatic login
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Endpoint for user login.
     * Maps to POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Calls the service layer to authenticate credentials and generate JWT
        return ResponseEntity.ok(authenticationService.authenticateAndGetToken(request));
    }
}