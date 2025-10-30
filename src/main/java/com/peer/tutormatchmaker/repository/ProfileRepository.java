package com.peer.tutormatchmaker.repository;

import com.peer.tutormatchmaker.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    /**
     * Finds a Profile associated with a specific User ID.
     */
    Optional<Profile> findByUserId(Long userId);
}