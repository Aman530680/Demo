package com.peer.tutormatchmaker.dto;

import lombok.Builder;
import lombok.Data;
import com.peer.tutormatchmaker.model.Role;

@Data
@Builder
public class AuthResponse {
    private String token; // The generated JWT
    private Long userId;
    private String name;
    private Role role;
    private String message;
}