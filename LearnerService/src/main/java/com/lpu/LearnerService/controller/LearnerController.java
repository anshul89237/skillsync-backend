package com.lpu.LearnerService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.dto.SkillDTO;
import com.lpu.LearnerService.entity.LearnerProfile;
import com.lpu.LearnerService.service.LearnerService;

@RestController
@RequestMapping("/learners")
public class LearnerController {

    private final LearnerService service;

    public LearnerController(LearnerService service) {
        this.service = service;
    }

    @PostMapping
    public LearnerProfile create(@RequestBody LearnerProfile profile) {
        return service.createProfile(profile);
    }

    @GetMapping("/{userId}")
    public LearnerProfileDTO get(@PathVariable Long userId) {
        return service.getProfile(userId);
    }

    @PostMapping("/{userId}/skills")
    public SkillDTO addSkill(@PathVariable Long userId, @RequestBody SkillDTO skill) {
        return service.addSkill(userId, skill);
    }
}