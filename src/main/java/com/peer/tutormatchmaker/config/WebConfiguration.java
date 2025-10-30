package com.peer.tutormatchmaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to enable Cross-Origin Resource Sharing (CORS).
 * This is necessary during development when the frontend (web page)
 * is served from a different port or location than the backend API.
 */
@Configuration
public class WebConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allows all origins (localhost:port, etc.) to make requests to the /api/** endpoints.
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        // Typically needed for JWT authentication headers and other custom headers
                        .allowedHeaders("*")
                        // Allows POST, GET, PUT, DELETE, etc.
                        .allowedMethods("*")
                        // Allows the browser to process credentials (cookies, auth headers)
                        .allowCredentials(false); // Set to true if credentials/sessions are used, but typically false for JWTs
            }
        };
    }
}
