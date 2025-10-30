package com.peer.tutormatchmaker.repository;

import com.peer.tutormatchmaker.model.TutorSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TutorSkillRepository extends JpaRepository<TutorSkill, Long> {
    List<TutorSkill> findByTutorId(Long tutorId);
    List<TutorSkill> findBySubjectContainingIgnoreCase(String subject);
}
