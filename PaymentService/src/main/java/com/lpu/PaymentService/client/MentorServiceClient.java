package com.lpu.PaymentService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lpu.PaymentService.dto.MentorDTO;
import com.lpu.java.common_security.dto.ApiResponse;

@FeignClient(name = "MENTOR-SERVICE", path = "/api/v1/mentors")
public interface MentorServiceClient {

	@GetMapping("/{id}")
	public ApiResponse<MentorDTO> findMentorById(@PathVariable("id") Long id);

}
