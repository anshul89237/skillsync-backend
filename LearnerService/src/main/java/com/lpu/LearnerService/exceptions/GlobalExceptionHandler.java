package com.lpu.LearnerService.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException exception) {
		Map<String, String> map = new HashMap<>();
		map.put("Error", exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		Map<String, String> map = new HashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
	}
}
