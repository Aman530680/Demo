package com.peer.tutormatchmaker.model;

import jakarta.persistence.*;

/**
 * Feedback entity for collecting session ratings and comments.
 */
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id", nullable = false, unique = true)
    private Session session;

    @Column(nullable = false)
    private Integer rating; // 1 to 5

    @Column(columnDefinition = "TEXT")
    private String comments;

    // Default Constructor
    public Feedback() {}

    // Constructor
    public Feedback(Session session, Integer rating, String comments) {
        this.session = session;
        this.rating = rating;
        this.comments = comments;
    }

    // --- Standard Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
