package com.lpu.ReviewService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.ReviewService.client.MentorServiceClient;
import com.lpu.ReviewService.client.UserServiceClient;
import com.lpu.ReviewService.dto.MentorDTO;
import com.lpu.ReviewService.dto.UsersDTO;
import com.lpu.ReviewService.entity.Review;
import com.lpu.ReviewService.service.ReviewService;

@RequestMapping("/reviews")
@RestController
public class ReviewController {

	@Autowired
	private ReviewService service;

	@Autowired
	private UserServiceClient userClient;

	@Autowired
	private MentorServiceClient mentorClient;

	@PostMapping
	public Review saveReview(@RequestBody Review review) {


	    UsersDTO user = userClient.findUserById(review.getUser_id());
	    MentorDTO mentor = mentorClient.findMentorById(review.getMentorId());

	    if (user == null) {
	        throw new RuntimeException(
	                "No learner found with id: " + review.getUser_id());
	    }

	    if (mentor == null) {
	        throw new RuntimeException(
	                "No mentor found with id: " + review.getMentorId());
	    }

	    review.setCreated_at(LocalDateTime.now());

	    // Step 1: Save review
	    Review savedReview = service.saveReview(review);

	    // Step 2: Get average rating
	    Double avgRating = service.getAverageRating(review.getMentorId());

	    // Step 3: Update mentor rating
	    mentorClient.updateMentorRating(review.getMentorId(), avgRating);

	    System.out.println("Review added & mentor rating updated");

	    return savedReview;
	}

	@GetMapping("/{id}")
	public Review findReviewById(@PathVariable("id") Long id) {
		return service.findReviewById(id);
	}

	@GetMapping
	public List<Review> findAllReviews() {
		return service.findAllReviews();
	}

	@PutMapping("/{id}")
	public Review updateReviewById(@PathVariable Long id, @RequestBody Review review) {
		return service.updateReviewById(id, review);
	}

	@DeleteMapping("/{id}")
	public void deleteReviewById(@PathVariable Long id) {
		service.deleteReviewById(id);
	}
}
