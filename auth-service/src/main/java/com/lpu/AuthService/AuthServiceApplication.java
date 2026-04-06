package com.lpu.AuthService;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
 
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
        "com.lpu.AuthService",
        "com.lpu.java.common_security"
})
public class AuthServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing AuthService Application...");
		SpringApplication.run(AuthServiceApplication.class, args);
		logger.info("AuthService Application started successfully.");
	}
 
}
