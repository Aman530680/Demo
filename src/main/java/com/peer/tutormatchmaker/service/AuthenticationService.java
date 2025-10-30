package com.peer.tutormatchmaker.service;
import com.peer.tutormatchmaker.service.UserService;
import com.peer.tutormatchmaker.dto.LoginRequest;
import com.peer.tutormatchmaker.dto.RegisterRequest;
import com.peer.tutormatchmaker.dto.AuthResponse;
import com.peer.tutormatchmaker.model.Role;
import com.peer.tutormatchmaker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Helper method to perform user registration without generating a token.
     * This method contains the core user creation/role defaulting logic.
     */
    public User registerUserOnly(RegisterRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole() != null ? request.getRole() : Role.STUDENT
        );
        return userService.registerNewUser(user);
    }

    /**
     * Registers a new user and automatically logs them in (Original flow, used by API endpoints).
     */
    public AuthResponse register(RegisterRequest request) {
        User registeredUser = registerUserOnly(request); // Reuse core logic

        // Immediately generate a token after registration
        String jwt = generateTokenForUser(registeredUser);

        return AuthResponse.builder()
                .token(jwt)
                .userId(registeredUser.getId())
                .name(registeredUser.getName())
                .role(registeredUser.getRole())
                .message("Registration successful. User automatically logged in.")
                .build();
    }

    /**
     * Authenticates the user credentials and generates a JWT.
     */
    public AuthResponse authenticateAndGetToken(LoginRequest request) {
        // 1. Authenticate credentials using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Retrieve the User entity by casting the UserDetailsAdapter
        User user = retrieveUserFromSecurityContext(request.getEmail());

        // 3. Generate token and return AuthResponse
        String jwt = generateTokenForUser(user);

        return AuthResponse.builder()
                .token(jwt)
                .userId(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .message("Login successful.")
                .build();
    }

    // Helper to correctly extract the User entity from our custom adapter
    private User retrieveUserFromSecurityContext(String email) {
        UserDetails userDetails = userService.loadUserByUsername(email);
        UserService.UserDetailsAdapter adapter = (UserService.UserDetailsAdapter) userDetails;
        return adapter.user;
    }

    // Helper to generate the JWT.
    private String generateTokenForUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());
        return jwtService.generateToken(claims, user);
    }
}