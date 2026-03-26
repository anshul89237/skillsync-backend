package com.lpu.AuthService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lpu.AuthService.config.JwtUtil;
import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.LoginResponse;
import com.lpu.AuthService.entity.Users;
import com.lpu.AuthService.exception.InvalidCredentialsException;
import com.lpu.AuthService.exception.UserNotFoundException;
import com.lpu.AuthService.repository.UserRepository;

@Service
public class UserService 
{

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	public Users saveUser(Users user) 
	{
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}

	public LoginResponse login(LoginRequest request) 
	{
		Users user = repository.findUserByEmail(request.getEmail());

		if (user == null) 
			throw new UserNotFoundException("No account found with email: " + request.getEmail());

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
			throw new InvalidCredentialsException("Invalid email or password");

		String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
		return new LoginResponse(token, user.getRole(), user.getEmail(), user.getId());
	}
	

	public Users findUserByMail(String email) 
	{
		Users user = repository.findUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}
		return user;
	}

	
	public Users findUserById(Long id) 
	{
		return repository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
	}

	
	public List<Users> findUsers() {
		return repository.findAll();
	}

	public Users updateUserById(Long id, Users user) 
	{
		Users u = repository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
		u.setName(user.getName());
		u.setEmail(user.getEmail());
		
		if (user.getPassword() != null && !user.getPassword().isBlank())
			u.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return repository.save(u);
	}

	public String deleteUserById(Long id)
	{
		repository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
		
		repository.deleteById(id);
		
		return "User Deleted Successfully"; 
	}

	public Users updateRole(Long id, String role) 
	{
		Users user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		
		user.setRole(role);
		
		return repository.save(user);
	}
}