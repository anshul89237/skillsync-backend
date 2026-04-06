package com.lpu.LearnerService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.java.common_security.config.SecurityUtils;
import com.lpu.LearnerService.service.LearnerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/learners")
@RequiredArgsConstructor
public class LearnerController {

	private static final Logger logger = LoggerFactory.getLogger(LearnerController.class);

    private final LearnerService learnerService;

    @PostMapping
    public LearnerProfileDTO create(@RequestBody LearnerProfileDTO profileDTO) {
		Long userId = SecurityUtils.getCurrentUserId();
		logger.info("Request to create learner profile for user ID: {}", userId);
        // 🔐 Zero Trust: Extract learner ID directly from current authenticated JWT context
        profileDTO.setUserId(userId);
        return learnerService.createProfile(profileDTO);
    }

    @GetMapping("/{userId}")
    public LearnerProfileDTO get(@PathVariable Long userId) {
		logger.info("Request to fetch learner profile for user ID: {}", userId);
        return learnerService.getProfile(userId);
    }

        @PutMapping("/self")
        public LearnerProfileDTO update(@RequestBody LearnerProfileDTO profileDTO) {
            Long userId = SecurityUtils.getCurrentUserId();
            logger.info("Request to update learner profile for user ID: {}", userId);
            return learnerService.updateProfile(userId, profileDTO);
        }

        @PostMapping("/self/skills")
    public void addSkill(@RequestParam Long skillId) {
		Long userId = SecurityUtils.getCurrentUserId();
		logger.info("Request to add skill ID {} to self (User ID: {})", skillId, userId);
        // 🔐 Zero Trust: Extract user ID directly from current authenticated JWT context
        learnerService.addSkill(userId, skillId);
    }

    @DeleteMapping("/self/skills")
    public void removeSkill(@RequestParam Long skillId) {
		Long userId = SecurityUtils.getCurrentUserId();
		logger.info("Request to remove skill ID {} from self (User ID: {})", skillId, userId);
        // 🔐 Zero Trust: Extract user ID directly from current authenticated JWT context
        learnerService.removeSkill(userId, skillId);
    }
}