package com.lpu.SessionService;
 
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
	    "com.lpu.SessionService",
	    "com.lpu.java.common_security"
	})
public class SessionServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(SessionServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing SessionService Application...");
		SpringApplication.run(SessionServiceApplication.class, args);
		logger.info("SessionService Application started successfully.");
	}
 
}
