package com.lpu.SessionService.exception;

public class UserNotFoundException extends RuntimeException 
{
 
	public UserNotFoundException(String message) {
        super(message);
    }
}