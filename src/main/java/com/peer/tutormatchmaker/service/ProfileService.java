package com.peer.tutormatchmaker.service;

import com.peer.tutormatchmaker.dto.ProfileRequest;
import com.peer.tutormatchmaker.dto.TutorSkillDto;
import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import com.peer.tutormatchmaker.model.Profile;
import com.peer.tutormatchmaker.model.TutorSkill;
import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.model.Role;
import com.peer.tutormatchmaker.repository.ProfileRepository;
import com.peer.tutormatchmaker.repository.TutorSkillRepository;
import com.peer.tutormatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final TutorSkillRepository tutorSkillRepository;

    @Transactional
    public Profile createOrUpdateProfile(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }

        profile.setSubjects(request.getSubjects());
        profile.setAvailability(request.getAvailability());

        Profile savedProfile = profileRepository.save(profile);
        user.setProfile(savedProfile);
        userRepository.save(user);

        if (user.getRole() == Role.TUTOR) {
            updateTutorSkills(userId, request.getSkills());
        }

        return savedProfile;
    }

    public Optional<Profile> getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Transactional
    public void updateTutorSkills(Long tutorId, List<TutorSkillDto> skillDtos) {
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found with ID: " + tutorId));

        if (tutor.getRole() != Role.TUTOR) {
            throw new IllegalArgumentException("User is not a Tutor.");
        }

        // 1. Delete existing skills
        List<TutorSkill> existingSkills = tutorSkillRepository.findByTutorId(tutorId);
        tutorSkillRepository.deleteAll(existingSkills);

        // 2. Add new skills
        if (skillDtos != null && !skillDtos.isEmpty()) {
            List<TutorSkill> newSkills = skillDtos.stream().map(dto -> {
                TutorSkill skill = new TutorSkill();
                skill.setTutor(tutor);
                skill.setSubject(dto.getSubject());
                skill.setLevel(dto.getLevel());
                return skill;
            }).collect(Collectors.toList());
            tutorSkillRepository.saveAll(newSkills);
        }
    }

    /**
     * Retrieves all TutorSkill DTOs for a specific tutor ID.
     * @param tutorId The ID of the tutor.
     * @return A list of TutorSkillDto objects.
     */
    public List<TutorSkillDto> getTutorSkills(Long tutorId) {
        return tutorSkillRepository.findByTutorId(tutorId).stream()
                .map(skill -> {
                    TutorSkillDto dto = new TutorSkillDto();
                    dto.setId(skill.getId());
                    dto.setSubject(skill.getSubject());
                    dto.setLevel(skill.getLevel());
                    return dto;
                }).collect(Collectors.toList());
    }
}
