package com.peer.tutormatchmaker.repository;

import com.peer.tutormatchmaker.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findBySessionTutorId(Long tutorId);
    Optional<Feedback> findBySessionId(Long sessionId);
}
