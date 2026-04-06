package com.lpu.LearnerService.service;

import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.entity.LearnerSkill;

public interface LearnerService {

    LearnerProfileDTO createProfile(LearnerProfileDTO profileDTO);

    LearnerProfileDTO updateProfile(Long userId, LearnerProfileDTO profileDTO);

    LearnerSkill addSkill(Long userId, Long skillId);

    void removeSkill(Long userId, Long skillId);

    LearnerProfileDTO getProfile(Long userId);
}
