package com.lpu.MentorService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.MentorService.client.SkillServiceClient;
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.service.MentorService;
import com.lpu.MentorService.service.MentorSkillService;

@RequestMapping("/mentorskill")
@RestController
public class MentorSkillController {

	@Autowired
	private MentorSkillService service;
	
	@Autowired
	private MentorService serv;
	
	@Autowired
	private SkillServiceClient client;
	
	@PostMapping
	public MentorSkill saveMentorSkill(@RequestBody MentorSkill mentorSkill) {
		
		SkillDTO skill = client.findSkillById(mentorSkill.getSkill_id());
		Mentor mentor = serv.findMentorById(mentorSkill.getMentor_id());

		if (skill == null) {
			throw new RuntimeException("No skill found with skill id : " + mentorSkill.getSkill_id() + ". Create skill");
		}
		
		if (mentor == null) {
			throw new RuntimeException("No mentor found with mentor id : " + mentorSkill.getMentor_id() + ". Create mentor account");
		}
		
		return service.saveMentorSkill(mentorSkill);
	}
	
	@GetMapping("/{id}")
	public MentorSkill findMentorSkillById(@PathVariable("id") Long id) {
		return service.findMentorSkillById(id);
	}
	
	@GetMapping
	public List<MentorSkill> findAllMentorSkills() {
		return service.findMentorSkills();
	}
	
	@DeleteMapping("/{id}")
	public void deleteMentorSkillById(@PathVariable("id") Long id) {
		service.deleteMentorSkillById(id);
	}
}
