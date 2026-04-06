package com.lpu.ReviewService.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.java.common_security.config.SecurityUtils;
import com.lpu.ReviewService.dto.ReviewDTO;
import com.lpu.ReviewService.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

	private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

	private final ReviewService reviewService;

	@PostMapping
	public ReviewDTO saveReview(@RequestBody ReviewDTO reviewDTO) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		logger.info("Review submission received from User ID: {} for Mentor ID: {}", currentUserId, reviewDTO.getMentorId());

		// 🔐 Zero Trust: Extract learner ID directly from current authenticated JWT context
		reviewDTO.setUser_id(currentUserId);

	    return reviewService.saveReview(reviewDTO);
	}

	@GetMapping("/{id}")
	public ReviewDTO findReviewById(@PathVariable("id") Long id) {
		logger.info("Request to fetch review by ID: {}", id);
		return reviewService.findReviewById(id);
	}

	@GetMapping
	public List<ReviewDTO> findAllReviews() {
		logger.info("Request to fetch all reviews");
		return reviewService.findAllReviews();
	}

	@PutMapping("/{id}")
	public ReviewDTO updateReviewById(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
		logger.info("Request to update review ID: {}", id);
		return reviewService.updateReviewById(id, reviewDTO);
	}

	@DeleteMapping("/{id}")
	public void deleteReviewById(@PathVariable Long id) {
		logger.info("Request to delete review ID: {}", id);
		reviewService.deleteReviewById(id);
	}
	
	@GetMapping("/byMentor/{mentorId}")
	public List<ReviewDTO> findReviewByMentorId(@PathVariable Long mentorId) {
		logger.info("Request to fetch reviews for Mentor ID: {}", mentorId);
		return reviewService.getReviewsByMentorId(mentorId);
	}
}
