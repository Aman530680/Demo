package com.peer.tutormatchmaker.service;

import com.peer.tutormatchmaker.dto.FeedbackRequest;
import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import com.peer.tutormatchmaker.model.Feedback;
import com.peer.tutormatchmaker.model.Profile;
import com.peer.tutormatchmaker.model.Session;
import com.peer.tutormatchmaker.model.Session.SessionStatus;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.repository.FeedbackRepository;
import com.peer.tutormatchmaker.repository.ProfileRepository;
import com.peer.tutormatchmaker.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final NotificationService notificationService;

    /**
     * Submits new feedback for a completed session and updates the tutor's rating.
     * @param request The feedback details.
     * @param studentId The ID of the student submitting the feedback.
     * @return The saved Feedback entity.
     */
    @Transactional
    public Feedback submitFeedback(FeedbackRequest request, Long studentId) {
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + request.getSessionId()));

        // Validation 1: Check if the session is completed
        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalArgumentException("Feedback can only be submitted for COMPLETED sessions.");
        }

        // Validation 2: Check if the student is the correct person to submit feedback
        if (!session.getStudent().getId().equals(studentId)) {
            throw new SecurityException("You are not authorized to leave feedback for this session.");
        }

        // Validation 3: Check if feedback already exists for this session
        if (feedbackRepository.findBySessionId(request.getSessionId()).isPresent()) {
            throw new IllegalArgumentException("Feedback already exists for this session.");
        }

        // 1. Create and save the feedback
        Feedback feedback = new Feedback(session, request.getRating(), request.getComments());
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // 2. Update the tutor's average rating
        updateTutorRating(session.getTutor());

        // 3. Notify the tutor
        notificationService.createNotification(
                session.getTutor(),
                String.format("You received a %d-star rating from %s for session ID %d.",
                        request.getRating(), session.getStudent().getName(), session.getId()),
                "FEEDBACK_RECEIVED"
        );

        return savedFeedback;
    }

    /**
     * Retrieves all feedback submitted for a specific tutor.
     * @param tutorId The ID of the tutor.
     * @return A list of Feedback entities.
     */
    public List<Feedback> getFeedbackByTutorId(Long tutorId) {
        return feedbackRepository.findBySessionTutorId(tutorId);
    }

    /**
     * Calculates the new average rating for a tutor based on all submitted feedback.
     * @param tutor The User entity of the tutor.
     */
    private void updateTutorRating(User tutor) {
        List<Feedback> allFeedback = feedbackRepository.findBySessionTutorId(tutor.getId());

        double averageRating = allFeedback.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0); // Default to 0.0 if no ratings exist

        // Find and update the tutor's profile
        Profile profile = profileRepository.findByUserId(tutor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for tutor ID: " + tutor.getId()));

        // Update rating to one decimal place for display purposes
        long roundedRating = Math.round(averageRating * 10);
        profile.setRating((double) roundedRating / 10);

        profileRepository.save(profile);
    }
}