package com.lpu.MentorService.exception;

public class UserNotFoundException extends RuntimeException 
{
    
	public UserNotFoundException(String message) {
        super(message);
    }
}