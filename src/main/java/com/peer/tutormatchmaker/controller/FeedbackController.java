package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.dto.FeedbackRequest;
import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import com.peer.tutormatchmaker.model.Feedback;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.service.FeedbackService;
import com.peer.tutormatchmaker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // Helper to get current authenticated user's ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResourceNotFoundException("No authenticated user found.");
        }
        // Assuming we rely on the UserDetailsAdapter wrapper class in UserService
        return ((UserService.UserDetailsAdapter) authentication.getPrincipal()).user.getId();
    }

    /**
     * Endpoint for students to submit feedback for a completed session.
     * Requires: session ID, rating (1-5), and optional comments.
     */
    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        Long studentId = getCurrentUserId();
        Feedback feedback = feedbackService.submitFeedback(request, studentId);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<Feedback>> getFeedbackForTutor(@PathVariable Long tutorId) {
        // Note: For production, we'd add checks to ensure the requester is the tutor/admin.
        // For simplicity, we assume the service layer handles security validation if needed.
        // FIX: Call the new service method, getFeedbackByTutorId(tutorId).
        List<Feedback> feedbackList = feedbackService.getFeedbackByTutorId(tutorId);
        return ResponseEntity.ok(feedbackList);
    }
}