package com.lpu.AuthService.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.AuthService.dto.UpdateProfileRequest;
import com.lpu.AuthService.dto.UpdateSettingsRequest;
import com.lpu.AuthService.dto.UsersDTO;
import com.lpu.AuthService.dto.response.UserSettingsResponse;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.service.UserService;
import com.lpu.java.common_security.dto.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/users")
@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private UsersDTO toUsersDTO(User user) {
        if (user == null) {
            return null;
        }

        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setCreated_at(user.getCreatedAt());
        return dto;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UsersDTO>> findUserByMail(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {

        return ResponseEntity.ok(ApiResponse.success("User found by email", toUsersDTO(userService.findUserByMail(email))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsersDTO>> findUserById(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id) {

        return ResponseEntity.ok(ApiResponse.success("User found by ID", toUsersDTO(userService.findUserById(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsersDTO>>> findAllUsers() {
        List<UsersDTO> users = userService.findUsers().stream().map(this::toUsersDTO).toList();
        return ResponseEntity.ok(ApiResponse.success("All users fetched", users));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<User>> updateProfile(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id,
            @RequestBody @Valid UpdateProfileRequest request) {

        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", userService.updateProfile(id, request)));
    }

    @PutMapping("/{id}/settings")
    public ResponseEntity<ApiResponse<User>> updateSettings(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id,
            @RequestBody @Valid UpdateSettingsRequest request) {

        return ResponseEntity.ok(ApiResponse.success("Settings updated successfully", userService.updateSettings(id, request)));
    }

    @GetMapping("/{id}/settings")
    public ResponseEntity<ApiResponse<UserSettingsResponse>> getSettings(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id) {

        return ResponseEntity.ok(ApiResponse.success("Settings fetched successfully", userService.getSettings(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsersDTO>> updateUserById(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id,

            @RequestBody @Valid User user) {

        return ResponseEntity
            .ok(ApiResponse.success("User updated successfully", toUsersDTO(userService.updateUserById(id, user))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUserById(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id) {

        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", userService.deleteUserById(id)));
    }

    @PutMapping("/{id}/role/{role}")
    public ResponseEntity<ApiResponse<UsersDTO>> updateUserRole(
            @PathVariable @NotNull(message = "User ID is required") @Positive(message = "User ID must be positive") Long id,

            @PathVariable @NotBlank(message = "Role is required") @Pattern(regexp = "^(ROLE_)?(USER|ADMIN|LEARNER|MENTOR)$", message = "Role must be USER, ADMIN, LEARNER, or MENTOR") String role) {

        return ResponseEntity
            .ok(ApiResponse.success("User role updated successfully", toUsersDTO(userService.updateRole(id, role))));
    }

    @PostMapping("/{userId}/skills/{skillId}")
    public ResponseEntity<ApiResponse<Void>> addSkillToUser(@PathVariable Long userId, @PathVariable Long skillId) {
        userService.addSkillToUser(userId, skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill added to user", null));
    }

    @DeleteMapping("/{userId}/skills/{skillId}")
    public ResponseEntity<ApiResponse<Void>> removeSkillFromUser(@PathVariable Long userId,
            @PathVariable Long skillId) {
        userService.removeSkillFromUser(userId, skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill removed from user", null));
    }

    @GetMapping("/{userId}/skills")
    public ResponseEntity<ApiResponse<List<Long>>> getUserSkills(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("User skills fetched", userService.getUserSkills(userId)));
    }
}