package com.lpu.ReviewService.service;

import java.util.List;
import com.lpu.ReviewService.dto.ReviewDTO;

public interface ReviewService {

    ReviewDTO saveReview(ReviewDTO reviewDTO);

    ReviewDTO updateReviewById(Long id, ReviewDTO reviewDTO);

    void deleteReviewById(Long id);

    ReviewDTO findReviewById(Long id);

    List<ReviewDTO> findAllReviews();

    List<ReviewDTO> getReviewsByMentorId(Long mentorId);

    Double getAverageRating(Long mentorId);
}
