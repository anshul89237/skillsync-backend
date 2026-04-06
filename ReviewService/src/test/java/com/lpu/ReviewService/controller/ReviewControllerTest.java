package com.lpu.ReviewService.controller;

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
import com.lpu.ReviewService.dto.ReviewDTO;
import com.lpu.ReviewService.service.ReviewService;
import com.lpu.java.common_security.config.JwtUtil;
import com.lpu.java.common_security.config.SecurityUtils;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

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
    void shouldSaveReview() throws Exception {
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setMentorId(10L);
        reviewDTO.setRating(5.0);
        
        when(reviewService.saveReview(any(ReviewDTO.class))).thenReturn(reviewDTO);

        mockMvc.perform(post("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rating").value(5.0));
    }

    @Test
    void shouldReturnReviewById() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setRating(5.0);
        when(reviewService.findReviewById(1L)).thenReturn(reviewDTO);

        mockMvc.perform(get("/api/v1/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void shouldUpdateReview() throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(4.0);
        when(reviewService.updateReviewById(anyLong(), any(ReviewDTO.class))).thenReturn(reviewDTO);

        mockMvc.perform(put("/api/v1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rating").value(4.0));
    }

    @Test
    void shouldDeleteReview() throws Exception {
        mockMvc.perform(delete("/api/v1/reviews/1"))
                .andExpect(status().isOk());
        
        verify(reviewService).deleteReviewById(1L);
    }
}
