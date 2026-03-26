package com.lpu.ReviewService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.ReviewService.client.MentorServiceClient;
import com.lpu.ReviewService.entity.Review;
import com.lpu.ReviewService.exception.ReviewNotFoundException;
import com.lpu.ReviewService.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;

	@Autowired
	private MentorServiceClient mentorClient;

	public Review saveReview(Review review) 
	{
		return repository.save(review);
	}

	public Review findReviewById(Long id) 
	{
		return repository.findById(id).orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
	}

	public List<Review> findAllReviews() 
	{
		return repository.findAll();
	}

	public Review updateReviewById(Long id, Review review) 
	{
		Review r = repository.findById(id).orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
		
		r.setRating(review.getRating());
		r.setComment(review.getComment());
		
		return repository.save(r);
	}

	public void deleteReviewById(Long id) 
	{
		repository.findById(id).orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
		
		repository.deleteById(id);
	}

	public void updateMentorRating(Long mentorId) 
	{
		Double avgRating = repository.findAverageRatingByMentorId(mentorId);
		
		if (avgRating == null)
			avgRating = 0.0;
		
		mentorClient.updateMentorRating(mentorId, avgRating);
	}

	public Double getAverageRating(Long mentorId) 
	{
		return repository.findAverageRatingByMentorId(mentorId);
	}
}