package com.lpu.NotificationService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/notify")
public class NotificationController {

    @GetMapping("/test")
    public String test() {
        return "Notification Service Running";
    }
}
