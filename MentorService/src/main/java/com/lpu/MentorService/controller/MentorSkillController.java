package com.lpu.MentorService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.MentorService.dto.MentorSkillDTO;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.service.MentorSkillService;

@RequestMapping("/mentorskill")
@RestController
public class MentorSkillController {

	@Autowired
	private MentorSkillService service;

	@PostMapping
	public MentorSkill addSkillToMentor(@RequestBody MentorSkillDTO mentorSkill) {
		return service.addSkillToMentor(mentorSkill);
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
