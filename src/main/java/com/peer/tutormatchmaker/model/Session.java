package com.peer.tutormatchmaker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Scheduling module: Represents a booked tutoring session.
 */
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String subject;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.PENDING;

    public enum SessionStatus {
        PENDING,
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }

    // Default Constructor
    public Session() {}

    // Constructor
    public Session(User tutor, User student, LocalDateTime dateTime, String subject) {
        this.tutor = tutor;
        this.student = student;
        this.dateTime = dateTime;
        this.subject = subject;
    }

    // --- Standard Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getTutor() { return tutor; }
    public void setTutor(User tutor) { this.tutor = tutor; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
}
