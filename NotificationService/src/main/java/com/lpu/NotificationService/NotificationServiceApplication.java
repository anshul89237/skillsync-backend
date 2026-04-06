package com.lpu.NotificationService;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
 
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(
	    scanBasePackages = {
	        "com.lpu.NotificationService",
	        "com.lpu.java.common_security.config"
	    },
	    exclude = {
	        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
	        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
	        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class,
	        org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration.class
	    }
	)
public class NotificationServiceApplication {
 
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);
 
	public static void main(String[] args) {
		logger.info("Initializing NotificationService Application...");
		SpringApplication.run(NotificationServiceApplication.class, args);
		logger.info("NotificationService Application started successfully.");
	}
 
}
