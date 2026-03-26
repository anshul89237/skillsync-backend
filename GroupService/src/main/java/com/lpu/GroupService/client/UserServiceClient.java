package com.lpu.GroupService.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lpu.GroupService.dto.UsersDTO;

@FeignClient(name = "authservice", path = "/users")
public interface UserServiceClient {

	@PostMapping
	public UsersDTO saveUser(@RequestBody UsersDTO user);
	
	@GetMapping("/email/{email}")
	public UsersDTO findUserByMail(@PathVariable("email") String email);
	
	@GetMapping("/id/{id}")
	public UsersDTO findUserById(@PathVariable("id") Long id);
	
	@GetMapping
	public List<UsersDTO> findAllUsers();
	
	@PutMapping("/{id}")
	public UsersDTO updateUserById(@PathVariable("id") Long id, @RequestBody UsersDTO user);
	
	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable("id") Long id);
}
