package com.lpu.MentorService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorSkillRepository;

@Service
public class MentorSkillQueryService {

	@Autowired
	private MentorSkillRepository repository;
	
	@Cacheable(value = "mentorSkill", key = "#id")
	public MentorSkill findMentorSkillById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Cacheable(value = "mentorSkillList")
	public List<MentorSkill> findMentorSkills() {
		return repository.findAll();
	}
}
