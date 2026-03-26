package com.lpu.MentorService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.MentorService.entity.MentorSkill;

@Repository
public interface MentorSkillRepository extends JpaRepository<MentorSkill, Long> {

}
