package com.lpu.NotificationService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lpu.NotificationService.dto.UsersDTO;


@FeignClient(name = "AuthService", path = "/users")
public interface UserServiceClient {
	
	@GetMapping("/id/{id}")
	public UsersDTO findUserById(@PathVariable("id") Long id);
	
	default String getUserEmail(Long id) {
        return findUserById(id).getEmail();
    }
	
}
