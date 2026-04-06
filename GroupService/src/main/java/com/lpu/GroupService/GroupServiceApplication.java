package com.lpu.GroupService;
 
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
	    "com.lpu.GroupService",
	    "com.lpu.java.common_security"
	})
public class GroupServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(GroupServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing GroupService Application...");
		SpringApplication.run(GroupServiceApplication.class, args);
		logger.info("GroupService Application started successfully.");
	}
 
}
