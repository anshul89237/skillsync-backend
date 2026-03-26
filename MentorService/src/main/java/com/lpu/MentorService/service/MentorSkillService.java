package com.lpu.MentorService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.MentorService.client.SkillServiceClient;
import com.lpu.MentorService.dto.MentorSkillDTO;
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorSkillRepository;

@Service
public class MentorSkillService {

	@Autowired
	private MentorSkillRepository repository;
	
	@Autowired
	private SkillServiceClient skillClient;
	
	 public MentorSkill addSkillToMentor(MentorSkillDTO dto) {
		 
		 	MentorSkill ms = new MentorSkill();
	        ms.setMentor_id(dto.getMentorId());   
	        ms.setSkill_id(dto.getSkillId());     

	        return repository.save(ms);
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
