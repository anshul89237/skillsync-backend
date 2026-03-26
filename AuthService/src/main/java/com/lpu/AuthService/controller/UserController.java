package com.lpu.AuthService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.AuthService.entity.Users;
import com.lpu.AuthService.service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/users")
@RestController
public class UserController 
{

	@Autowired
	private UserService service;

	@PostMapping
	public ResponseEntity<Users> saveUser(@Valid @RequestBody Users user)
	{
		return ResponseEntity.status(201).body(service.saveUser(user));
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Users> findUserByMail(@PathVariable String email) 
	{
		return ResponseEntity.ok(service.findUserByMail(email));
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<Users> findUserById(@PathVariable Long id) 
	{
		return ResponseEntity.ok(service.findUserById(id));
	}

	@GetMapping
	public ResponseEntity<List<Users>> findAllUsers() 
	{
		return ResponseEntity.ok(service.findUsers());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Users> updateUserById(@PathVariable Long id, @RequestBody Users user) 
	{
		return ResponseEntity.ok(service.updateUserById(id, user));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable Long id) 
	{
		return ResponseEntity.ok(service.deleteUserById(id));
	}

	@PutMapping("/role/{id}/{role}")
	public ResponseEntity<Users> updateUserRole(@PathVariable Long id, @PathVariable String role) 
	{
		return ResponseEntity.ok(service.updateRole(id, role));
	}
}