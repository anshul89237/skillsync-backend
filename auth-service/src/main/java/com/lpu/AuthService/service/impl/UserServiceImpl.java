package com.lpu.AuthService.service.impl;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lpu.AuthService.custom.exceptions.UserNotFoundException;
import com.lpu.AuthService.dto.UpdateProfileRequest;
import com.lpu.AuthService.dto.UpdateSettingsRequest;
import com.lpu.AuthService.dto.response.UserSettingsResponse;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;
import com.lpu.AuthService.service.UserService;
import com.lpu.java.common_security.config.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private User getUserOrThrowById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
    }

    private User getUserOrThrowByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findUserByMail(String email) {
        return getUserOrThrowByEmail(email);
    }

    @Override
    public User findUserById(Long id) {
        return getUserOrThrowById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findUsers() {
        return repository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'MENTOR', 'ADMIN')")
    public User updateUserById(Long id, User user) {
        User existingUser = getUserOrThrowById(id);

        if (user.getName() != null)
            existingUser.setName(user.getName());

        if (user.getEmail() != null)
            existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isBlank())
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(existingUser);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'LEARNER', 'MENTOR')")
    public User updateProfile(Long id, UpdateProfileRequest request) {
        User existingUser = getUserOrThrowById(id);
        String firstName = request.getFirstName().trim();
        String lastName = request.getLastName().trim();

        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setName(firstName + " " + lastName);
        existingUser.setHeadline(trimToNull(request.getHeadline()));
        existingUser.setLearningGoal(trimToNull(request.getLearningGoal()));

        if ("ROLE_USER".equals(existingUser.getRole())) {
            existingUser.setRole("ROLE_LEARNER");
        }

        return repository.save(existingUser);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'MENTOR', 'ADMIN')")
    public User updateSettings(Long id, UpdateSettingsRequest request) {
        User existingUser = getUserOrThrowById(id);

        String visibility = trimToNull(request.getProfileVisibility());
        if (visibility == null) {
            visibility = "learners";
        }

        String normalizedVisibility = visibility.toLowerCase();
        if (!normalizedVisibility.equals("public") && !normalizedVisibility.equals("learners") && !normalizedVisibility.equals("private")) {
            normalizedVisibility = "learners";
        }

        existingUser.setEmailNotifications(request.isEmailNotifications());
        existingUser.setSessionReminders(request.isSessionReminders());
        existingUser.setMarketingUpdates(request.isMarketingUpdates());
        existingUser.setTwoFactorEnabled(request.isTwoFactorEnabled());
        existingUser.setProfileVisibility(normalizedVisibility);

        return repository.save(existingUser);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'MENTOR', 'ADMIN')")
    public UserSettingsResponse getSettings(Long id) {
        User user = getUserOrThrowById(id);
        UserSettingsResponse response = new UserSettingsResponse();
        response.setEmailNotifications(Boolean.TRUE.equals(user.getEmailNotifications()));
        response.setSessionReminders(Boolean.TRUE.equals(user.getSessionReminders()));
        response.setMarketingUpdates(Boolean.TRUE.equals(user.getMarketingUpdates()));
        response.setTwoFactorEnabled(Boolean.TRUE.equals(user.getTwoFactorEnabled()));

        String visibility = trimToNull(user.getProfileVisibility());
        if (visibility == null) {
            visibility = "learners";
        }
        response.setProfileVisibility(visibility.toLowerCase());

        return response;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUserById(Long id) {
        User user = getUserOrThrowById(id);
        repository.delete(user);
        return "User Deleted Successfully";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public User updateRole(Long id, String role) {
        User user = getUserOrThrowById(id);
        user.setRole(role);
        return repository.save(user);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'MENTOR', 'ADMIN')")
    public void addSkillToUser(Long userId, Long skillId) {
        User user = getUserOrThrowById(userId);
        if (!user.getSkillIds().contains(skillId)) {
            user.getSkillIds().add(skillId);
            repository.save(user);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'MENTOR', 'ADMIN')")
    public void removeSkillFromUser(Long userId, Long skillId) {
        User user = getUserOrThrowById(userId);
        user.getSkillIds().remove(skillId);
        repository.save(user);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'LEARNER', 'MENTOR', 'ADMIN')")
    public List<Long> getUserSkills(Long userId) {
        User user = getUserOrThrowById(userId);
        return user.getSkillIds();
    }
}
