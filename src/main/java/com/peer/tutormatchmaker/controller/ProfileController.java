package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.dto.ProfileRequest;
import com.peer.tutormatchmaker.dto.UserDto;
import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import com.peer.tutormatchmaker.model.Profile;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.service.ProfileService;
import com.peer.tutormatchmaker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    // Helper to get current authenticated user's ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResourceNotFoundException("No authenticated user found.");
        }
        return ((UserService.UserDetailsAdapter) authentication.getPrincipal()).user.getId();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile() {
        Long userId = getCurrentUserId();
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        Optional<Profile> profileOptional = profileService.getProfileByUserId(userId);
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            dto.setSubjects(profile.getSubjects());
            dto.setAvailability(profile.getAvailability());
            dto.setRating(profile.getRating());
        }

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me")
    public ResponseEntity<Profile> updateMyProfile(@Valid @RequestBody ProfileRequest request) {
        Long userId = getCurrentUserId();
        Profile updatedProfile = profileService.createOrUpdateProfile(userId, request);
        return ResponseEntity.ok(updatedProfile);
    }
}