package com.peer.tutormatchmaker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity for system notifications (session updates, etc.).
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String type; // e.g., "SESSION_CONFIRMATION", "FEEDBACK_RECEIVED"

    private LocalDateTime timestamp = LocalDateTime.now();

    private Boolean isRead = false;

    // Default Constructor
    public Notification() {}

    // Constructor
    public Notification(User user, String message, String type) {
        this.user = user;
        this.message = message;
        this.type = type;
    }

    // --- Standard Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}
