package com.peer.tutormatchmaker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class ProfileRequest {
    @NotBlank
    private String subjects;

    @NotBlank
    private String availability;

    private List<TutorSkillDto> skills;
}