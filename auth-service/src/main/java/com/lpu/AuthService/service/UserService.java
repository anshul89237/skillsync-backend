package com.lpu.AuthService.service;

import java.util.List;

import com.lpu.AuthService.dto.UpdateProfileRequest;
import com.lpu.AuthService.dto.UpdateSettingsRequest;
import com.lpu.AuthService.dto.response.UserSettingsResponse;
import com.lpu.AuthService.entity.User;

public interface UserService {

    User findUserByMail(String email);

    User findUserById(Long id);

    List<User> findUsers();

    User updateUserById(Long id, User user);

    User updateProfile(Long id, UpdateProfileRequest request);

    User updateSettings(Long id, UpdateSettingsRequest request);

    UserSettingsResponse getSettings(Long id);

    String deleteUserById(Long id);

    User updateRole(Long id, String role);

    void addSkillToUser(Long userId, Long skillId);

    void removeSkillFromUser(Long userId, Long skillId);

    List<Long> getUserSkills(Long userId);
}