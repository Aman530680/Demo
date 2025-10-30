package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import com.peer.tutormatchmaker.model.Notification;
import com.peer.tutormatchmaker.service.NotificationService;
import com.peer.tutormatchmaker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Helper to get current authenticated user's ID (reused from other controllers)
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResourceNotFoundException("No authenticated user found.");
        }
        // Assuming we rely on the UserDetailsAdapter wrapper class in UserService
        return ((UserService.UserDetailsAdapter) authentication.getPrincipal()).user.getId();
    }

    /**
     * Retrieves all notifications for the authenticated user, ordered by timestamp descending.
     * Maps to GET /api/v1/notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications() {
        Long userId = getCurrentUserId();
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marks a specific notification as read.
     * Maps to POST /api/v1/notifications/{notificationId}/read
     */
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}