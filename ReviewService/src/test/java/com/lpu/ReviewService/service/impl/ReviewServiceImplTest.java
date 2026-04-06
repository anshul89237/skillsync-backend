package com.lpu.ReviewService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.ReviewService.client.MentorServiceClient;
import com.lpu.ReviewService.dto.ReviewDTO;
import com.lpu.ReviewService.entity.Review;
import com.lpu.ReviewService.exception.ReviewNotFoundException;
import com.lpu.ReviewService.mapper.ReviewMapper;
import com.lpu.ReviewService.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository repository;
    @Mock
    private MentorServiceClient mentorClient;
    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId(1L);
        review.setUserId(1L);
        review.setMentorId(10L);
        review.setRating(4.5);
        review.setComment("Great mentor!");

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setMentorId(10L);
        reviewDTO.setRating(4.5);
        reviewDTO.setComment("Great mentor!");
    }

    @Test
    void shouldSaveReview_andUpdateMentorRating() {
        // given
        when(reviewMapper.toEntity(any(ReviewDTO.class))).thenReturn(review);
        when(repository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toDto(any(Review.class))).thenReturn(reviewDTO);
        when(repository.findAverageRatingByMentorId(10L)).thenReturn(4.5);

        // when
        ReviewDTO saved = reviewService.saveReview(reviewDTO);

        // then
        assertNotNull(saved);
        verify(repository).save(review);
        verify(mentorClient).updateMentorRating(10L, 4.5);
    }

    @Test
    void shouldUpdateReview_andUpdateMentorRating() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(review));
        when(repository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toDto(any(Review.class))).thenReturn(reviewDTO);
        when(repository.findAverageRatingByMentorId(10L)).thenReturn(4.5);

        // when
        ReviewDTO updated = reviewService.updateReviewById(1L, reviewDTO);

        // then
        assertNotNull(updated);
        verify(mentorClient).updateMentorRating(10L, 4.5);
    }

    @Test
    void shouldDeleteReview_andUpdateMentorRating() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(review));
        when(repository.findAverageRatingByMentorId(10L)).thenReturn(0.0);

        // when
        reviewService.deleteReviewById(1L);

        // then
        verify(repository).deleteById(1L);
        verify(mentorClient).updateMentorRating(10L, 0.0);
    }

    @Test
    void shouldThrowException_whenReviewNotFound() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ReviewNotFoundException.class, () -> reviewService.findReviewById(1L));
    }

    @Test
    void shouldReturnAverageRating() {
        // given
        when(repository.findAverageRatingByMentorId(10L)).thenReturn(4.5);

        // when
        Double result = reviewService.getAverageRating(10L);

        // then
        assertEquals(4.5, result);
    }
}
