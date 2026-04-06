package com.lpu.AuthService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lpu.AuthService.custom.exceptions.UserNotFoundException;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;
import com.lpu.java.common_security.config.JwtUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AuthUserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole("USER");
        user.setSkillIds(new ArrayList<>());
    }

    @Test
    void shouldReturnUser_whenIdExists() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // when
        User result = userService.findUserById(1L);

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void shouldUpdateUser_whenValidInput() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);

        User updateDetails = new User();
        updateDetails.setName("New Name");
        updateDetails.setPassword("newPass");

        // when
        User result = userService.updateUserById(1L, updateDetails);

        // then
        assertEquals("New Name", result.getName());
        verify(passwordEncoder).encode("newPass");
    }

    @Test
    void shouldAddSkillToUser() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // when
        userService.addSkillToUser(1L, 10L);

        // then
        assertTrue(user.getSkillIds().contains(10L));
        verify(repository).save(user);
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }
}
