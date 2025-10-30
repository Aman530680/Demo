package com.peer.tutormatchmaker.dto;

import com.peer.tutormatchmaker.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    // Role can be null, in which case the service defaults it to STUDENT
    private Role role;
}