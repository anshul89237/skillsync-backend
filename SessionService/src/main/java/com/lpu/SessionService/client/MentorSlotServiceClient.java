package com.lpu.SessionService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "mentorservice", path = "/mentor-slots", contextId = "mentorSlotClient")
public interface MentorSlotServiceClient {

    @GetMapping("/{id}")
    public com.lpu.SessionService.dto.MentorSlotDTO getSlot(@PathVariable("id") Long id);

    @PutMapping("/{id}/increment")
    public Object incrementSlot(@PathVariable("id") Long id);
}
