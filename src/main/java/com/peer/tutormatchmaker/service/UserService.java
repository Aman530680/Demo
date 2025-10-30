package com.peer.tutormatchmaker.service;

import com.peer.tutormatchmaker.model.User;
import com.peer.tutormatchmaker.model.Role;
import com.peer.tutormatchmaker.model.Profile;
import com.peer.tutormatchmaker.repository.UserRepository;
import com.peer.tutormatchmaker.repository.ProfileRepository;
import com.peer.tutormatchmaker.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    // --- Core UserDetailsService Implementation ---

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserDetailsAdapter(user);
    }

    // --- Custom User CRUD Operations ---

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User registerNewUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Automatically create a blank profile for every new user
        Profile profile = new Profile();
        profile.setUser(savedUser);
        profileRepository.save(profile);
        // Link the profile back to the user entity before returning
        savedUser.setProfile(profile);

        return savedUser;
    }

    @Transactional
    public User update(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // --- Spring Security Adapter Class ---

    /**
     * Adapter to bridge the JPA User entity with Spring Security's UserDetails contract.
     * This nested class is referenced in AuthenticationService for extracting the User entity.
     */
    public static class UserDetailsAdapter implements UserDetails {
        public final User user;

        public UserDetailsAdapter(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }

        @Override public String getPassword() { return user.getPassword(); }
        @Override public String getUsername() { return user.getEmail(); }
        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return true; }
    }
}