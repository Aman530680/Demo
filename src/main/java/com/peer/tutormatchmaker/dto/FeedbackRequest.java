package com.peer.tutormatchmaker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for collecting feedback/rating submitted by a student after a session.
 */
@Data
public class FeedbackRequest {

    @NotNull(message = "Session ID is required.")
    private Long sessionId;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating must be at most 5.")
    private Integer rating; // 1 to 5 star rating

    private String comments;
}
