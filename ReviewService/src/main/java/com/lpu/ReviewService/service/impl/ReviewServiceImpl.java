package com.lpu.ReviewService.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lpu.ReviewService.client.MentorServiceClient;
import com.lpu.ReviewService.dto.ReviewDTO;
import com.lpu.ReviewService.entity.Review;
import com.lpu.ReviewService.exception.ReviewNotFoundException;
import com.lpu.ReviewService.mapper.ReviewMapper;
import com.lpu.ReviewService.repository.ReviewRepository;
import com.lpu.ReviewService.service.ReviewService;

import lombok.RequiredArgsConstructor;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository repository;
    private final MentorServiceClient mentorClient;
    private final ReviewMapper reviewMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    @CachePut(value = "review", key = "#result.id")
    @CacheEvict(value = {"reviewsByMentor", "allReviews"}, allEntries = true)
    public ReviewDTO saveReview(ReviewDTO reviewDTO) {
		logger.info("Saving new review for Mentor ID: {} from User ID: {}", reviewDTO.getMentorId(), reviewDTO.getUser_id());
		Review review = reviewMapper.toEntity(reviewDTO);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = repository.save(review);
		logger.info("Successfully saved review with ID: {}", saved.getId());

		logger.debug("Triggering mentor rating update for Mentor ID: {}", saved.getMentorId());
	    updateMentorRating(saved.getMentorId());
	    return reviewMapper.toDto(saved);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CachePut(value = "review", key = "#id")
    @CacheEvict(value = {"reviewsByMentor", "allReviews"}, allEntries = true)
    public ReviewDTO updateReviewById(Long id, ReviewDTO reviewDTO) {
		logger.info("Updating review ID: {}", id);
	    Review r = repository.findById(id)
	        .orElseThrow(() -> {
				logger.error("Update failed: Review ID {} not found", id);
				return new ReviewNotFoundException("Review not found with id: " + id);
			});
	    r.setRating(reviewDTO.getRating());
	    r.setComment(reviewDTO.getComment());
	    Review updated = repository.save(r);
		logger.info("Successfully updated review ID: {}", id);
		logger.debug("Triggering mentor rating update for Mentor ID: {} after review update", updated.getMentorId());
	    updateMentorRating(updated.getMentorId());
	    return reviewMapper.toDto(updated);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CacheEvict(value = {"review", "reviewsByMentor", "allReviews"}, key = "#id", allEntries = true)
    public void deleteReviewById(Long id) {
		logger.info("Deleting review ID: {}", id);
	    Review r = repository.findById(id)
	        .orElseThrow(() -> {
				logger.error("Deletion failed: Review ID {} not found", id);
				return new ReviewNotFoundException("Review not found with id: " + id);
			});
	    repository.deleteById(id);
		logger.info("Successfully deleted review ID: {}", id);
		logger.debug("Triggering mentor rating update for Mentor ID: {} after review deletion", r.getMentorId());
	    updateMentorRating(r.getMentorId());
    }

    @Override
    @Cacheable(value = "review", key = "#id", unless = "#result == null")
    public ReviewDTO findReviewById(Long id) {
		logger.info("Fetching review details by ID: {}", id);
		Review review = repository.findById(id)
				.orElseThrow(() -> {
					logger.error("Review lookup failed: ID {} not found", id);
					return new ReviewNotFoundException("Review not found with id: " + id);
				});
        return reviewMapper.toDto(review);
    }

    @Override
    @Cacheable(value = "allReviews", unless = "#result == null || #result.isEmpty()")
    public List<ReviewDTO> findAllReviews() {
		logger.info("Fetching all reviews from the database");
		List<Review> reviews = repository.findAll();
		logger.info("Found {} total reviews", reviews.size());
		return reviewMapper.toDtoList(reviews);
    }

    @Override
    @Cacheable(value = "reviewsByMentor", key = "#mentorId", unless = "#result == null || #result.isEmpty()")
    public List<ReviewDTO> getReviewsByMentorId(Long mentorId) {
		logger.info("Fetching reviews for Mentor ID: {}", mentorId);
		List<Review> reviews = repository.findByMentorId(mentorId);
		logger.info("Found {} reviews for mentor ID: {}", reviews.size(), mentorId);
		return reviewMapper.toDtoList(reviews);
    }

    @Override
    public Double getAverageRating(Long mentorId) {
		logger.info("Calculating average rating for mentor ID: {}", mentorId);
        Double avgRating = repository.findAverageRatingByMentorId(mentorId);
		logger.info("Average rating for mentor ID {} is: {}", mentorId, avgRating);
		return avgRating;
    }

	@CircuitBreaker(name = "mentorService", fallbackMethod = "fallbackUpdateMentorRating")
	@Retry(name = "mentorService")
	private void updateMentorRating(Long mentorId) {
		logger.info("Calculating new weight-averaged rating for mentor ID: {}", mentorId);
	    Double avgRating = repository.findAverageRatingByMentorId(mentorId);
	    if (avgRating == null) {
			logger.warn("No reviews remaining for mentor ID: {}. Resetting average rating to 0.0", mentorId);
			avgRating = 0.0;
		}
		logger.debug("Syncing updated rating with MentorService for mentor ID: {}...", mentorId);
	    mentorClient.updateMentorRating(mentorId, avgRating);
		logger.info("Successfully synchronized rating for mentor ID: {}", mentorId);
	}

	public void fallbackUpdateMentorRating(Long mentorId, Exception ex) {
		logger.error("Fallback triggered for updateMentorRating for Mentor ID: {} due to: {}", mentorId, ex.getMessage());
	}
}
