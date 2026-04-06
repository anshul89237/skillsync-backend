package com.lpu.ReviewService.mapper;

import com.lpu.ReviewService.dto.ReviewDTO;
import com.lpu.ReviewService.entity.Review;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {
    public ReviewDTO toDto(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setMentorId(review.getMentorId());
        dto.setUser_id(review.getUserId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreated_at(review.getCreatedAt());
        return dto;
    }

    public Review toEntity(ReviewDTO reviewDTO) {
        if (reviewDTO == null) {
            return null;
        }

        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setMentorId(reviewDTO.getMentorId());
        review.setUserId(reviewDTO.getUser_id());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        return review;
    }

    public List<ReviewDTO> toDtoList(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return Collections.emptyList();
        }
        return reviews.stream().map(this::toDto).collect(Collectors.toList());
    }
}
