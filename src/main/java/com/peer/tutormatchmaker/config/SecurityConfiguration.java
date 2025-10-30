package com.peer.tutormatchmaker.config;

import com.peer.tutormatchmaker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// FIX: Use wildcard import to robustly resolve all classes in the config package,
// including JwtAuthenticationFilter, to resolve "cannot find symbol" error.
import com.peer.tutormatchmaker.config.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter; // Now properly resolved
    private final UserService userService; // UserDetailsService implementation
    private final PasswordEncoder passwordEncoder; // Injected from ApplicationConfig

    // Defines the Security Filter Chain (Authorization rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Allow static files and the core pages for Thymeleaf rendering
                        .requestMatchers("/", "/index.html", "/login", "/register", "/dashboard", "/css/**", "/js/**").permitAll()

                        // FIX: Explicitly permit POST requests to the custom login form handler
                        // This ensures the login attempt itself is not blocked by the filter chain.
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/login-form").permitAll()

                        // Allow access to public API endpoints for authentication
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Allow access to secured API endpoints for tutors/sessions
                        .requestMatchers("/api/v1/tutors/**", "/api/v1/sessions/**").authenticated()

                        // Allow access to Swagger/OpenAPI documentation
                        .requestMatchers("/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").permitAll()

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        // Use stateless sessions (required for JWT)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Define the custom authentication provider
                .authenticationProvider(authenticationProvider())
                // Add the JWT filter before the standard Spring Security filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Defines the authentication provider (uses UserService and PasswordEncoder)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Use the UserService as the UserDetailsService
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // Defines the global Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}