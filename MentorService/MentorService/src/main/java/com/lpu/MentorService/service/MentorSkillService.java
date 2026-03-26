package com.lpu.MentorService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorSkillRepository;

@Service
public class MentorSkillService {

	@Autowired
	private MentorSkillRepository repository;
	
	public MentorSkill saveMentorSkill(MentorSkill mentorSkill) {
		return repository.save(mentorSkill);
	}
	
	public MentorSkill findMentorSkillById(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public List<MentorSkill> findMentorSkills() {
		return repository.findAll();
	}
	
	public void deleteMentorSkillById(Long id) {
		repository.deleteById(id);
	}
	
}
