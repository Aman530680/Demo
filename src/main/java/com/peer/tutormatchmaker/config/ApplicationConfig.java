package com.peer.tutormatchmaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for application-wide beans.
 * Only the PasswordEncoder is defined here to avoid conflicts with SecurityConfiguration,
 * which correctly handles UserDetailsService, AuthenticationProvider, and AuthenticationManager.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Defines the PasswordEncoder bean, used to encode and verify user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt is the standard, secure hashing algorithm for passwords
        return new BCryptPasswordEncoder();
    }
}
