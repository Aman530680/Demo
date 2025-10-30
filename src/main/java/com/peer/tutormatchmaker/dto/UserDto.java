package com.peer.tutormatchmaker.dto;

import com.peer.tutormatchmaker.model.Role;
import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String subjects;
    private String availability;
    private Double rating;
    private List<TutorSkillDto> skills;
}