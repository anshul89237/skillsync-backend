package com.lpu.SkillService.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler 
{

	@ExceptionHandler(SkillNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleSkillNotFoundException(SkillNotFoundException ex) 
	{
		Map<String, String> map = new HashMap<>();
	
		map.put("error", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) 
	{
		Map<String, String> map = new HashMap<>();
	
		map.put("error", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) 
	{
		Map<String, String> map = new HashMap<>();
	
		map.put("error", ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleException(Exception ex) 
	{
		Map<String, String> map = new HashMap<>();
	
		map.put("error", "An unexpected error occurred: " + ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
	}
}