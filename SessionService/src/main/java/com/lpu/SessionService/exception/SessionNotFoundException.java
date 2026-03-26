package com.lpu.SessionService.exception;

public class SessionNotFoundException extends RuntimeException 
{
 
	public SessionNotFoundException(String message) {
        super(message);
    }
}