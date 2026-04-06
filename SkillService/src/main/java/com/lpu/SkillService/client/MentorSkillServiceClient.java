package com.lpu.SkillService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.lpu.SkillService.dto.MentorSkillDTO;

@FeignClient(name = "MentorService", path = "/mentorskill")
public interface MentorSkillServiceClient {

	@PostMapping
	public MentorSkillDTO addSkillToMentor(@RequestBody MentorSkillDTO mentorSkill);

	@GetMapping("/{id}")
	public MentorSkillDTO findMentorSkillById(@PathVariable("id") Long id);

	@GetMapping
	public List<MentorSkillDTO> findAllMentorSkills();

	@DeleteMapping("/{id}")
	public void deleteMentorSkillById(@PathVariable("id") Long id);
}
