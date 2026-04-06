package com.lpu.ReviewService;
 
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
	    "com.lpu.ReviewService",
	    "com.lpu.java.common_security"
	})
public class ReviewServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing ReviewService Application...");
		SpringApplication.run(ReviewServiceApplication.class, args);
		logger.info("ReviewService Application started successfully.");
	}
 
}
