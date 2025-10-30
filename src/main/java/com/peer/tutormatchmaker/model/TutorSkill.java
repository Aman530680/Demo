package com.peer.tutormatchmaker.model;

import jakarta.persistence.*;

/**
 * Entity to store specific skills a TUTOR offers.
 */
@Entity
@Table(name = "tutor_skills")
public class TutorSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor; // Must be a TUTOR role

    private String subject; // e.g., "Java", "Calculus"
    private String level;   // e.g., "Beginner", "Advanced"

    // Default Constructor
    public TutorSkill() {}

    // Constructor
    public TutorSkill(User tutor, String subject, String level) {
        this.tutor = tutor;
        this.subject = subject;
        this.level = level;
    }

    // --- Standard Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getTutor() { return tutor; }
    public void setTutor(User tutor) { this.tutor = tutor; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
