package com.lpu.SkillService;
 
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
	    "com.lpu.SkillService",
	    "com.lpu.java.common_security"
	})
public class SkillServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(SkillServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing SkillService Application...");
		SpringApplication.run(SkillServiceApplication.class, args);
		logger.info("SkillService Application started successfully.");
	}
 
}
