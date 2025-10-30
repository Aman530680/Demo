package com.peer.tutormatchmaker.dto;

public class AuthenticationResponse {
    private String token;
    private Long userId;
    private String userRole;

    // Default Constructor
    public AuthenticationResponse() {}

    // All-args Constructor
    public AuthenticationResponse(String token, Long userId, String userRole) {
        this.token = token;
        this.userId = userId;
        this.userRole = userRole;
    }

    // --- Standard Getters and Setters ---
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
}
