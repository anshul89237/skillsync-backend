package com.lpu.SkillService.exception;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class SkillNotFoundException extends RuntimeException 
{
	private static final Logger logger = LoggerFactory.getLogger(SkillNotFoundException.class);
 
	public SkillNotFoundException(String message) {
        super(message);
		logger.warn("SkillNotFoundException thrown with message: {}", message);
    }
}