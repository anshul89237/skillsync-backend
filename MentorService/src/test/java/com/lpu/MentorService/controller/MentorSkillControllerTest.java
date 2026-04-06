package com.lpu.MentorService.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.MentorService.dto.MentorSkillDTO;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.service.MentorSkillCommandService;
import com.lpu.MentorService.service.MentorSkillQueryService;
import com.lpu.java.common_security.config.JwtUtil;

@WebMvcTest(MentorSkillController.class)
class MentorSkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MentorSkillCommandService commandService;

    @MockitoBean
    private MentorSkillQueryService queryService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddSkillToMentor() throws Exception {
        MentorSkillDTO dto = new MentorSkillDTO();
        dto.setMentorId(1L);
        dto.setSkillId(10L);
        
        MentorSkill result = new MentorSkill();
        when(commandService.addSkillToMentor(any(MentorSkillDTO.class))).thenReturn(result);

        mockMvc.perform(post("/mentorskill")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnMentorSkillById() throws Exception {
        MentorSkill result = new MentorSkill();
        result.setId(1L);
        when(queryService.findMentorSkillById(1L)).thenReturn(result);

        mockMvc.perform(get("/mentorskill/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMentorSkill() throws Exception {
        mockMvc.perform(delete("/mentorskill/1"))
                .andExpect(status().isOk());
        
        verify(commandService).deleteMentorSkillById(1L);
    }
}
