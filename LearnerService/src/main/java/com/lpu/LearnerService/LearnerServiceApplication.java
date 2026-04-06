package com.lpu.LearnerService;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
 
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
	    "com.lpu.LearnerService",
	    "com.lpu.java.common_security"
	})
public class LearnerServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(LearnerServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing LearnerService Application...");
		SpringApplication.run(LearnerServiceApplication.class, args);
		logger.info("LearnerService Application started successfully.");
	}
 
}
