package com.lpu.MentorService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lpu.MentorService.entity.Mentor;

import feign.Param;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long>{
	
	@Query("SELECT m FROM Mentor m WHERE m.user_id = :user_id")
	Mentor findByUser_id(@Param("user_id") Long user_id);
}
