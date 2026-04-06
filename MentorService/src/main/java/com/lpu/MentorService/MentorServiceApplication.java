package com.lpu.MentorService;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
 
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {
	    "com.lpu.MentorService",
	    "com.lpu.java.common_security"
	})
public class MentorServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(MentorServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing MentorService Application...");
		SpringApplication.run(MentorServiceApplication.class, args);
		logger.info("MentorService Application started successfully.");
	}
 
}
