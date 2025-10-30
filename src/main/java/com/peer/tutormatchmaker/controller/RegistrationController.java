package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.dto.RegisterRequest;
import com.peer.tutormatchmaker.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final AuthenticationService authenticationService;

    /**
     * Handles the POST request from the Thymeleaf registration form.
     * Maps to POST /register
     * After successful registration, it redirects to the login page as requested.
     */
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (bindingResult.hasErrors()) {
            // Return to form if validation fails (e.g., password too short)
            return "register";
        }

        try {
            // 1. Only register the user, DO NOT perform auto-login.
            authenticationService.registerUserOnly(request);

            // 2. Add success message and redirect to the login page
            redirectAttributes.addFlashAttribute("registrationSuccess", "Registration successful! Please log in with your new credentials.");

            // 3. Redirect to the login endpoint
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            // Catch domain-specific errors (e.g., User with email already exists)
            model.addAttribute("registrationError", e.getMessage());
            // Preserve the user's input data in case of error
            return "register";
        }
    }
}