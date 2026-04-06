package com.lpu.LearnerService.mapper;

import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.entity.LearnerProfile;
import org.springframework.stereotype.Component;

@Component
public class LearnerMapper {
    public LearnerProfileDTO toDto(LearnerProfile profile) {
        if (profile == null) {
            return null;
        }

        LearnerProfileDTO dto = new LearnerProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId());
        dto.setPhone(profile.getPhone());
        dto.setDob(profile.getDob());
        dto.setLocation(profile.getLocation());
        dto.setBio(profile.getBio());
        return dto;
    }

    public LearnerProfile toEntity(LearnerProfileDTO dto) {
        if (dto == null) {
            return null;
        }

        LearnerProfile profile = new LearnerProfile();
        profile.setId(dto.getId());
        profile.setUserId(dto.getUserId());
        profile.setPhone(dto.getPhone());
        profile.setDob(dto.getDob());
        profile.setLocation(dto.getLocation());
        profile.setBio(dto.getBio());
        return profile;
    }
}
