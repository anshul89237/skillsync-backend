package com.lpu.SkillService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.SkillService.client.UserServiceClient;
import com.lpu.SkillService.dto.UsersDTO;
import com.lpu.SkillService.entity.Skill;
import com.lpu.SkillService.service.SkillService;

@RequestMapping("/skills")
@RestController
public class SkillController {

	@Autowired
	private SkillService service;
	
	@Autowired
	private UserServiceClient client;
	
	@PostMapping
	public Skill saveSkill(@RequestBody Skill skill) {
		return service.saveSkill(skill);
	}
	
	@GetMapping("/{id}")
	public Skill findSkillById(@PathVariable("id") Long id) {
		return service.findSkillById(id);
	}
	
	@GetMapping
	public List<Skill> findAllSkills() {
		return service.findAllSkills();
	}
	
	@PutMapping("/{id}")
	public Skill updateSkillById(@PathVariable("id") Long id, @RequestBody Skill skill) {
		
		UsersDTO user = client.findUserById(skill.getUserId());
		
		if (user == null) {
			throw new RuntimeException("No user found with user id : " + skill.getUserId() + ". Create account" );
		}
		
		return service.updateSkillById(id, skill);
	}
	
	@DeleteMapping("/{id}")
	public void deleteSkillById(@PathVariable("id") Long id) {
		service.deleteSkillById(id);
	}
	
	@GetMapping("/userId/{userId}")
	public List<Skill> findSkillsByUserId(@PathVariable Long userId) {
		return service.findSkillByUserId(userId);
	}
	
}
