package com.peer.tutormatchmaker.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TutorSkillDto {

    private Long id;

    @NotBlank
    private String subject;

    @NotBlank
    private String level;
}