package com.lpu.SessionService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lpu.SessionService.dto.MentorDTO;
import com.lpu.SessionService.dto.UsersDTO;

@FeignClient(name = "mentorservice", path = "/mentors")
public interface MentorServiceClient {

	@PostMapping("/apply")
	public MentorDTO saveMentor(@RequestBody MentorDTO mentor);

	@GetMapping("/{id}")
	public MentorDTO findMentorById(@PathVariable("id") Long id);

	@GetMapping
	public List<MentorDTO> findAllMentors();

	@PutMapping("/{id}")
	public MentorDTO updateMentorById(@PathVariable("id") Long id, @RequestBody MentorDTO mentor);

	@PutMapping("/users/{user_id}")
	public UsersDTO updateUserDTOById(@PathVariable("user_id") Long user_id, @RequestBody UsersDTO user);
	
	// mentor availability

	@DeleteMapping("/{id}")
	public void deleteMentorById(@PathVariable("id") Long id);
}
