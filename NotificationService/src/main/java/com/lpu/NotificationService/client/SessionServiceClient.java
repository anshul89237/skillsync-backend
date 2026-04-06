package com.lpu.NotificationService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lpu.NotificationService.dto.SessionDTO;

@FeignClient(name = "SESSION-SERVICE")
public interface SessionServiceClient {

    @GetMapping("/api/sessions/{id}")
    SessionDTO findSessionById(@PathVariable("id") Long id);
}
