package com.lpu.MentorService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.lpu.MentorService.dto.MentorSkillDTO;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorSkillRepository;

@Service
public class MentorSkillCommandService {

	@Autowired
	private MentorSkillRepository repository;

	@CachePut(value = "mentorSkill", key = "#result.id")
	public MentorSkill addSkillToMentor(MentorSkillDTO dto) {

		MentorSkill ms = new MentorSkill();
		ms.setMentorId(dto.getMentorId());
		ms.setSkillId(dto.getSkillId());

		return repository.save(ms);
	}

	@CacheEvict(value = "mentorSkill", key = "#id")
	public void deleteMentorSkillById(Long id) {
		repository.deleteById(id);
	}

}
