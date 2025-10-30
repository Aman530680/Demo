package com.peer.tutormatchmaker.controller;

import com.peer.tutormatchmaker.dto.TutorSkillDto;
import com.peer.tutormatchmaker.dto.UserDto;
import com.peer.tutormatchmaker.model.Profile;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.repository.TutorSkillRepository;
import com.peer.tutormatchmaker.repository.UserRepository;
import com.peer.tutormatchmaker.service.ProfileService;
import com.peer.tutormatchmaker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorSkillRepository tutorSkillRepository;
    private final UserRepository userRepository;
    private final ProfileService profileService;

    /**
     * Searches for tutors by skill subject.
     * @param subject The subject to filter by (optional).
     * @return A list of UserDto representing matching tutors.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> searchTutors(@RequestParam(required = false) String subject) {
        List<UserDto> matchingTutors;

        if (subject != null && !subject.trim().isEmpty()) {
            // Find tutor IDs based on matching skill subject
            List<Long> tutorIds = tutorSkillRepository.findBySubjectContainingIgnoreCase(subject).stream()
                    .map(skill -> skill.getTutor().getId())
                    .distinct()
                    .toList();

            // Fetch user and profile data for those IDs
            List<User> users = userRepository.findAllById(tutorIds);

            matchingTutors = users.stream()
                    .map(user -> mapToUserDto(user, profileService.getProfileByUserId(user.getId()).orElse(null)))
                    .collect(Collectors.toList());

        } else {
            // If no subject, find all users who are Tutors (This is a simplified approach)
            // A more complex query would be needed for production use.
            List<User> allUsers = userRepository.findAll();
            matchingTutors = allUsers.stream()
                    .filter(user -> user.getRole().name().equals("TUTOR"))
                    .map(user -> mapToUserDto(user, profileService.getProfileByUserId(user.getId()).orElse(null)))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(matchingTutors);
    }

    // Helper method to map User and Profile entities to UserDto
    private UserDto mapToUserDto(User user, Profile profile) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        if (profile != null) {
            dto.setSubjects(profile.getSubjects());
            dto.setAvailability(profile.getAvailability());
            dto.setRating(profile.getRating());
            // Also include skills if the user is a TUTOR
            if (user.getRole().name().equals("TUTOR")) {
                dto.setSkills(profileService.getTutorSkills(user.getId()));
            }
        }
        return dto;
    }
}
