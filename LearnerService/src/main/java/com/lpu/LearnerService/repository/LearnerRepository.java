package com.lpu.LearnerService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.LearnerService.entity.LearnerProfile;

@Repository
public interface LearnerRepository extends JpaRepository<LearnerProfile, Long> {
	
    Optional<LearnerProfile> findByUserId(Long userId);
}