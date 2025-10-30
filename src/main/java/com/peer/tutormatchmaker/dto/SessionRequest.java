package com.peer.tutormatchmaker.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO for requesting a new session booking.
 */
@Data
public class SessionRequest {

    @NotNull(message = "Tutor ID is required")
    private Long tutorId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Date and time are required")
    @Future(message = "Session must be scheduled for a future time")
    private LocalDateTime dateTime;
}
