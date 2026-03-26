package com.lpu.LearnerService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lpu.LearnerService.dto.UsersDTO;

@FeignClient(name = "AuthService", path = "/users")
public interface UserServiceClient {

	@PostMapping
	UsersDTO saveUser(@RequestBody UsersDTO user);
	
	@GetMapping("/id/{id}")
	public UsersDTO findUserById(@PathVariable("id") Long id);
	
	@GetMapping("/email/{email}")
	UsersDTO findUserByMail(@PathVariable("email") String email);
	
	@PutMapping("/role/{id}/{role}")
	public UsersDTO updateUserRole(@PathVariable Long id, @PathVariable String role);
}
