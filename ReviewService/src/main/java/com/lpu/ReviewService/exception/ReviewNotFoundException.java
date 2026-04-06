package com.lpu.ReviewService.exception;

public class ReviewNotFoundException extends RuntimeException 
{
 
	public ReviewNotFoundException(String message) {
        super(message);
    }
}