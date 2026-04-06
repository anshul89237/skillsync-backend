package com.lpu.MentorService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.java.common_security.dto.ApiResponse;

@FeignClient(name = "AUTH-SERVICE", path = "/api/v1/users")
public interface UserServiceClient {

	@PostMapping
	public ApiResponse<UsersDTO> saveUser(@RequestBody UsersDTO user);
	
	@GetMapping("/email/{email}")
	public ApiResponse<UsersDTO> findUserByMail(@PathVariable("email") String email);
	
	@GetMapping("/{id}")
	public ApiResponse<UsersDTO> findUserById(@PathVariable("id") Long id);
	
	@GetMapping
	public ApiResponse<List<UsersDTO>> findAllUsers();
	
	@PutMapping("/{id}")
	public ApiResponse<UsersDTO> updateUserById(@PathVariable("id") Long id, @RequestBody UsersDTO user);
	
	@DeleteMapping("/{id}")
	public ApiResponse<String> deleteUserById(@PathVariable("id") Long id);
	
	@PutMapping("/{id}/role/{role}")
	ApiResponse<UsersDTO> updateUserRole(@PathVariable("id") Long id, @PathVariable("role") String role);
}
