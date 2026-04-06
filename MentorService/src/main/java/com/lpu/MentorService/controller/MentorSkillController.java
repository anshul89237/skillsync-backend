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

import com.lpu.MentorService.dto.MentorSkillDTO;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.service.MentorSkillCommandService;
import com.lpu.MentorService.service.MentorSkillQueryService;

@RequestMapping("/mentorskill")
@RestController
public class MentorSkillController {

	@Autowired
	private MentorSkillCommandService commandService;
	
	@Autowired
	private MentorSkillQueryService queryService;

	@PostMapping
	public MentorSkill addSkillToMentor(@RequestBody MentorSkillDTO mentorSkill) {
		return commandService.addSkillToMentor(mentorSkill);
	}

	@GetMapping("/{id}")
	public MentorSkill findMentorSkillById(@PathVariable("id") Long id) {
		return queryService.findMentorSkillById(id);
	}

	@GetMapping
	public List<MentorSkill> findAllMentorSkills() {
		return queryService.findMentorSkills();
	}

	@DeleteMapping("/{id}")
	public void deleteMentorSkillById(@PathVariable("id") Long id) {
		commandService.deleteMentorSkillById(id);
	}
}
