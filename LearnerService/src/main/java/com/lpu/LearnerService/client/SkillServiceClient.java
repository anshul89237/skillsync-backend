package com.lpu.LearnerService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lpu.LearnerService.dto.SkillDTO;

@FeignClient(name = "SkillService", path = "/skills")
public interface SkillServiceClient {

	@PostMapping
	public SkillDTO saveSkill(@RequestBody SkillDTO skill);
	
	@GetMapping("/{id}")
	public SkillDTO findSkillById(@PathVariable("id") Long id);
	
	@GetMapping
	public List<SkillDTO> findAllSkills();
	
	@PutMapping("/{id}")
	public SkillDTO updateSkillById(@PathVariable("id") Long id, @RequestBody SkillDTO skill);
	
	@DeleteMapping("/id")
	public void deleteSkillById(@PathVariable("id") Long id);
	
	@GetMapping("/userId/{userId}")
	public List<SkillDTO> findSkillsByUserId(@PathVariable Long userId);
	
}
