package com.lpu.ApiGateway;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
 
@EnableDiscoveryClient 
@SpringBootApplication
public class ApiGatewayApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(ApiGatewayApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing ApiGateway Application...");
		SpringApplication.run(ApiGatewayApplication.class, args);
		logger.info("ApiGateway Application started successfully.");
	}
 
}
