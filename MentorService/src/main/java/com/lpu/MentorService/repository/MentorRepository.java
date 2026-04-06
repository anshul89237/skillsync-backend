package com.lpu.MentorService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lpu.MentorService.entity.Mentor;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long>{
	
	Optional<Mentor> findByUserId(Long userId);

	List<Mentor> findByStatus(String status);
}
