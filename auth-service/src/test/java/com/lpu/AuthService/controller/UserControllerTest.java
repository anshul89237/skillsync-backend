package com.lpu.AuthService.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.service.UserService;
import com.lpu.java.common_security.config.JwtUtil;

@WebMvcTest(UserController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void shouldReturnUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        when(userService.findUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void shouldUpdateUserRole() throws Exception {
        User user = new User();
        user.setRole("ADMIN");
        when(userService.updateRole(1L, "ADMIN")).thenReturn(user);

        mockMvc.perform(put("/api/v1/users/1/role/ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        when(userService.deleteUserById(1L)).thenReturn("User Deleted Successfully");

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("User Deleted Successfully"));
    }

    @Test
    void shouldAddSkillToUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/1/skills/10"))
                .andExpect(status().isOk());
        
        verify(userService).addSkillToUser(1L, 10L);
    }
}
