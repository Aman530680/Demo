package com.peer.tutormatchmaker.service;

import com.peer.tutormatchmaker.dto.SessionRequest;
import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import com.peer.tutormatchmaker.model.Session;
import com.peer.tutormatchmaker.model.Session.SessionStatus;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.model.Role;
import com.peer.tutormatchmaker.repository.SessionRepository;
import com.peer.tutormatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService; // Use profileService for tutor data

    /**
     * Creates a new session requested by a student.
     * @param studentId The ID of the student booking the session.
     * @param request The session request details.
     * @return The newly created Session entity.
     */
    @Transactional
    public Session createSession(Long studentId, SessionRequest request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("Only students can book sessions.");
        }

        User tutor = userRepository.findById(request.getTutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found with ID: " + request.getTutorId()));

        if (tutor.getRole() != Role.TUTOR) {
            throw new IllegalArgumentException("The target user is not a tutor.");
        }

        Session session = new Session(
                tutor,
                student,
                request.getDateTime(),
                request.getSubject()
        );

        // All new sessions start as PENDING
        return sessionRepository.save(session);
    }

    /**
     * Retrieves all sessions for the authenticated user (both as student and tutor).
     * @param userId The ID of the user.
     * @return A list of relevant sessions.
     */
    public List<Session> getMySessions(Long userId) {
        return sessionRepository.findByTutorIdOrStudentId(userId, userId);
    }

    /**
     * Confirms a session, typically called by the tutor.
     * @param sessionId The ID of the session to confirm.
     * @param tutorId The ID of the tutor performing the confirmation.
     * @return The updated Session entity.
     */
    @Transactional
    public Session confirmSession(Long sessionId, Long tutorId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + sessionId));

        if (!session.getTutor().getId().equals(tutorId)) {
            throw new IllegalArgumentException("Only the assigned tutor can confirm this session.");
        }

        if (session.getStatus() != SessionStatus.PENDING) {
            throw new IllegalArgumentException("Session is already confirmed or cancelled.");
        }

        session.setStatus(SessionStatus.CONFIRMED);
        return sessionRepository.save(session);
    }

    // Additional methods like cancelSession, completeSession would be added here...
}
