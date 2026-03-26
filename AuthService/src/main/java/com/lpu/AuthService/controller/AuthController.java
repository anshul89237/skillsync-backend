package com.lpu.AuthService.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.LoginResponse;
import com.lpu.AuthService.entity.Users;
import com.lpu.AuthService.service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController
{

	@Autowired
	private UserService service;

	@PostMapping("/register")
	public ResponseEntity<Users> register(@Valid @RequestBody Users user) 
	{
		user.setRole("ROLE_USER");
		user.setCreated_at(LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) 
	{
		return ResponseEntity.ok(service.login(request));
	}
}