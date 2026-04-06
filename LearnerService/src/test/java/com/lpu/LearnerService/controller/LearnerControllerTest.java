package com.lpu.LearnerService.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.service.LearnerService;
import com.lpu.java.common_security.config.JwtUtil;
import com.lpu.java.common_security.config.SecurityUtils;

@WebMvcTest(LearnerController.class)
class LearnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LearnerService learnerService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private MockedStatic<SecurityUtils> mockedSecurityUtils;

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = mockStatic(SecurityUtils.class);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
    }

    @Test
    void shouldCreateProfile() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        LearnerProfileDTO profileDTO = new LearnerProfileDTO();
        profileDTO.setUserId(1L);
        profileDTO.setName("John Doe");
        
        when(learnerService.createProfile(any(LearnerProfileDTO.class))).thenReturn(profileDTO);

        mockMvc.perform(post("/api/v1/learners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profileDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnProfileDTO() throws Exception {
        LearnerProfileDTO dto = new LearnerProfileDTO();
        dto.setName("John Doe");
        dto.setSkills(Collections.emptyList());
        
        when(learnerService.getProfile(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/api/v1/learners/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddSkill() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        
        mockMvc.perform(post("/api/v1/learners/self/skills")
                .param("skillId", "10"))
                .andExpect(status().isOk());
        
        verify(learnerService).addSkill(1L, 10L);
    }

    @Test
    void shouldRemoveSkill() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        
        mockMvc.perform(delete("/api/v1/learners/self/skills")
                .param("skillId", "10"))
                .andExpect(status().isOk());
        
        verify(learnerService).removeSkill(1L, 10L);
    }
}
