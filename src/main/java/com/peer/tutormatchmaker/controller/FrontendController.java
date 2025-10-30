package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    // Maps the root URL to index.html
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Maps /login to login.html
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Maps /register to register.html
    @GetMapping("/register")
    public String register(Model model) {
        // Ensure the form backing object is in the model for Thymeleaf binding
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        return "register";
    }

    // Maps /dashboard to dashboard.html (Secured access managed by SecurityConfiguration)
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Spring Security handles access. Spring automatically makes flash attributes
        // (like jwtToken, userId) available in the model after a successful redirect.
        return "dashboard";
    }

    // Maps /profile to profile.html
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // Maps /session to session.html (for booking/rescheduling)
    @GetMapping("/session")
    public String session() {
        return "session";
    }

    // Maps /feedback to feedback.html (for submitting feedback)
    @GetMapping("/feedback")
    public String feedback() {
        return "feedback";
    }
}