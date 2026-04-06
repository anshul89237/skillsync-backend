package com.lpu.SkillService.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.SkillService.dto.SkillDTO;
import com.lpu.SkillService.service.SkillService;
import com.lpu.java.common_security.config.JwtUtil;

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SkillService skillService;

    @MockitoBean
    private JwtUtil jwtUtil; // Required for Security Filters

    @Autowired
    private ObjectMapper objectMapper;

    private SkillDTO skillDTO;

    @BeforeEach
    void setUp() {
        skillDTO = new SkillDTO();
        skillDTO.setId(1L);
        skillDTO.setName("Java");
        skillDTO.setCategoryId(1L);
    }

    @Test
    void shouldReturnSkill_whenIdExists() throws Exception {
        when(skillService.findSkillById(1L)).thenReturn(skillDTO);

        mockMvc.perform(get("/api/v1/skills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Java"));
    }

    @Test
    void shouldReturnAllSkills() throws Exception {
        when(skillService.findAllSkills()).thenReturn(Arrays.asList(skillDTO));

        mockMvc.perform(get("/api/v1/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Java"));
    }

    @Test
    void shouldCreateSkill() throws Exception {
        when(skillService.saveSkill(any(SkillDTO.class))).thenReturn(skillDTO);

        mockMvc.perform(post("/api/v1/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateSkill() throws Exception {
        when(skillService.updateSkillById(anyLong(), any(SkillDTO.class))).thenReturn(skillDTO);

        mockMvc.perform(put("/api/v1/skills/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteSkill() throws Exception {
        mockMvc.perform(delete("/api/v1/skills/1"))
                .andExpect(status().isOk());
    }
}
