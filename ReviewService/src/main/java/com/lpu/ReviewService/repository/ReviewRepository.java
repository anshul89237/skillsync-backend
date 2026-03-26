package com.lpu.ReviewService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lpu.ReviewService.entity.Review;

import feign.Param;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.mentorId = :mentorId")
	Double findAverageRatingByMentorId(@Param("mentorId") Long mentorId);
	
}
