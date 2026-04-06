package com.lpu.PaymentService;
 
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
	    "com.lpu.PaymentService",
	    "com.lpu.java.common_security"
	})
public class PaymentServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing PaymentService Application...");
		SpringApplication.run(PaymentServiceApplication.class, args);
		logger.info("PaymentService Application started successfully.");
	}
 
}
