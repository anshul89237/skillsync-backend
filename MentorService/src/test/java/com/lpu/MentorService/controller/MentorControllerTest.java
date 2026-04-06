package com.lpu.MentorService.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.MentorDTO;
import com.lpu.MentorService.dto.MentorProfileDTO;
import com.lpu.MentorService.service.MentorService;
import com.lpu.java.common_security.config.JwtUtil;
import com.lpu.java.common_security.config.SecurityUtils;


@WebMvcTest(MentorController.class)
class MentorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MentorService mentorService;

    @MockitoBean
    private UserServiceClient client;

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
    void shouldSubmitApplication_whenValidInput() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setUserId(1L);
        
        when(mentorService.saveMentor(any(MentorDTO.class))).thenReturn(mentorDTO);

        mockMvc.perform(post("/api/v1/mentors/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mentorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application submitted successfully, pending approval"));
    }

    @Test
    void shouldReturnProfile_whenUserIdExists() throws Exception {
        MentorProfileDTO dto = new MentorProfileDTO();
        dto.setName("Jane Doe");
        
        when(mentorService.getMentorProfile(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/mentors/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Jane Doe"));
    }

    @Test
    void shouldApproveApplication() throws Exception {
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setStatus("APPROVED");
        when(mentorService.approveApplication(1L)).thenReturn(mentorDTO);

        mockMvc.perform(put("/api/v1/mentors/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    @Test
    void shouldAddSkill() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        
        mockMvc.perform(post("/api/v1/mentors/self/skills")
                .param("skillId", "10"))
                .andExpect(status().isOk());
    }
}
