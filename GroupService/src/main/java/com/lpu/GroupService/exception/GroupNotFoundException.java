package com.lpu.GroupService.exception;

public class GroupNotFoundException extends RuntimeException {
	
	public GroupNotFoundException(String message) 
	{
		super(message);
	}
}