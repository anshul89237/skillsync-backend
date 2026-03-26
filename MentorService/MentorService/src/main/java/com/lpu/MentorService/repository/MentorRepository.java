package com.lpu.MentorService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.MentorService.entity.Mentor;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long>{

	
}
