package com.lpu.SessionService.controller;

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
import com.lpu.SessionService.dto.SessionDTO;
import com.lpu.SessionService.service.SessionService;
import com.lpu.java.common_security.config.JwtUtil;
import com.lpu.java.common_security.config.SecurityUtils;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

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
    void shouldCreateSession() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setMentorId(10L);
        
        when(sessionService.createSession(any(SessionDTO.class))).thenReturn(sessionDTO);

        mockMvc.perform(post("/api/v1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateStatus_andTransitionToScheduled_whenAccepted() throws Exception {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setStatus("SCHEDULED");
        when(sessionService.updateStatus(anyLong(), eq("SCHEDULED"))).thenReturn(sessionDTO);

        mockMvc.perform(put("/api/v1/sessions/1/ACCEPTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SCHEDULED"));
    }

    @Test
    void shouldDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/v1/sessions/1"))
                .andExpect(status().isOk());
        
        verify(sessionService).deleteSessionById(1L);
    }
}
