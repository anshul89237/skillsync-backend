package com.lpu.AuthService.exception;

public class InvalidCredentialsException extends RuntimeException 
{
	
	public InvalidCredentialsException(String message) {
		super(message);
	}
}