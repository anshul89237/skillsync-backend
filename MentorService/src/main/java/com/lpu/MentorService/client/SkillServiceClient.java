package com.lpu.MentorService.client;
 
import java.util.List;
 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
 
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.java.common_security.dto.ApiResponse;
 
@FeignClient(name = "SkillService", path = "/api/v1/skills")
public interface SkillServiceClient {
 
	@GetMapping("/batch")
	ApiResponse<List<SkillDTO>> findSkillsByIds(@RequestParam("ids") List<Long> ids);
	
	@GetMapping("/{id}")
	ApiResponse<SkillDTO> findSkillById(@PathVariable("id") Long id);
	
	@GetMapping
	ApiResponse<List<SkillDTO>> findAllSkills();
	
	@DeleteMapping("/{id}")
	ApiResponse<Void> deleteSkillById(@PathVariable("id") Long id);
 
}
