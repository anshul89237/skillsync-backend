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
import com.lpu.SkillService.dto.CategoryDTO;
import com.lpu.SkillService.service.SkillService;
import com.lpu.java.common_security.config.JwtUtil;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SkillService skillService;

    @MockitoBean
    private JwtUtil jwtUtil; // Required for Security Filters

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Programming");
    }

    @Test
    void shouldReturnCategory_whenIdExists() throws Exception {
        when(skillService.findCategoryById(1L)).thenReturn(categoryDTO);

        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Programming"));
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        when(skillService.findAllCategories()).thenReturn(Arrays.asList(categoryDTO));

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Programming"));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        when(skillService.saveCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        when(skillService.updateCategory(anyLong(), any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(put("/api/v1/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isOk());
    }
}
