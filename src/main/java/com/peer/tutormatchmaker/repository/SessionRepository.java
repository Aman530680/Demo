package com.peer.tutormatchmaker.repository;

import com.peer.tutormatchmaker.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTutorId(Long tutorId);
    List<Session> findByStudentId(Long studentId);
    List<Session> findByTutorIdOrStudentId(Long tutorId, Long studentId);
}
