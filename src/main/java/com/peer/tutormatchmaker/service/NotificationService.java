package com.peer.tutormatchmaker.service;

import com.peer.tutormatchmaker.model.Notification;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Creates and saves a new notification for a specific user.
     * @param user The recipient of the notification.
     * @param message The content of the notification.
     * @param type The type of notification (e.g., "SESSION_CONFIRMED").
     * @return The saved Notification entity.
     */
    public Notification createNotification(User user, String message, String type) {
        Notification notification = new Notification(user, message, type);
        return notificationRepository.save(notification);
    }

    /**
     * Retrieves all notifications for the given user, ordered by timestamp descending.
     * @param userId The ID of the user whose notifications to retrieve.
     * @return A list of notifications.
     */
    public List<Notification> getNotificationsForUser(Long userId) {
        // Implementation relies on the custom repository method
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Marks a specific notification as read.
     * @param notificationId The ID of the notification to mark as read.
     */
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }
}
