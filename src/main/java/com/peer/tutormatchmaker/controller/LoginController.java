package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.dto.AuthResponse;
import com.peer.tutormatchmaker.dto.LoginRequest;
import com.peer.tutormatchmaker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;

    /**
     * Handles the POST request from the custom Thymeleaf login form (/login-form).
     * It manually calls the JWT AuthenticationService and redirects to the dashboard with token data.
     * Maps to POST /login-form
     */
    @PostMapping("/login-form")
    public String processLoginForm(@RequestParam("username") String email,
                                   @RequestParam("password") String password,
                                   RedirectAttributes redirectAttributes) {

        try {
            // 1. Authenticate using the JWT service
            LoginRequest request = new LoginRequest(email, password);
            AuthResponse authResponse = authenticationService.authenticateAndGetToken(request); //

            // 2. Pass necessary information (JWT, ID, Role) to the Dashboard via flash attributes
            redirectAttributes.addFlashAttribute("jwtToken", authResponse.getToken());
            redirectAttributes.addFlashAttribute("userId", authResponse.getUserId());
            redirectAttributes.addFlashAttribute("userRole", authResponse.getRole().name());
            redirectAttributes.addFlashAttribute("loginSuccess", authResponse.getMessage());

            // 3. Redirect to the secure dashboard endpoint
            return "redirect:/dashboard";

        } catch (Exception e) {
            // Catch authentication failure (e.g., BadCredentialsException)
            // Redirect back to login with an error parameter
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login?error";
        }
    }
}