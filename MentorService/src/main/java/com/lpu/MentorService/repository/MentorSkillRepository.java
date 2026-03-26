package com.lpu.MentorService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.MentorService.entity.MentorSkill;

@Repository
public interface MentorSkillRepository extends JpaRepository<MentorSkill, Long> {

	boolean existsByMentorIdAndSkillId(Long mentorId, Long skillId);
	
	 List<MentorSkill> findByMentorId(Long mentorId);
}
