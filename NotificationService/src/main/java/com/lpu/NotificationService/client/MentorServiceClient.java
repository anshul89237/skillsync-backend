package com.lpu.NotificationService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lpu.NotificationService.dto.MentorDTO;

@FeignClient(name = "mentorservice", path = "/mentors")
public interface MentorServiceClient {

	@GetMapping("/{id}")
	public MentorDTO findMentorById(@PathVariable("id") Long id);


}
