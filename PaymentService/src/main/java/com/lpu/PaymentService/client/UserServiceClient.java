package com.lpu.PaymentService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lpu.PaymentService.dto.UsersDTO;

@FeignClient(name = "AuthService", path = "/users")
public interface UserServiceClient {
	
	@GetMapping("/id/{id}")
	public UsersDTO findUserById(@PathVariable("id") Long id);
	
}
