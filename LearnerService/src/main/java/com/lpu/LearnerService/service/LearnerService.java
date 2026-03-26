package com.lpu.LearnerService.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lpu.LearnerService.client.SkillServiceClient;
import com.lpu.LearnerService.client.UserServiceClient;
import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.dto.SkillDTO;
import com.lpu.LearnerService.dto.UsersDTO;
import com.lpu.LearnerService.entity.LearnerProfile;
import com.lpu.LearnerService.repository.LearnerRepository;

@Service
public class LearnerService {

    private final LearnerRepository repo;
    private final UserServiceClient userClient;
    private final SkillServiceClient skillClient;

    public LearnerService(LearnerRepository repo, UserServiceClient userClient, SkillServiceClient skillClient) {
        this.repo = repo;
        this.userClient = userClient;
        this.skillClient = skillClient;
    }

    public LearnerProfile createProfile(LearnerProfile profile) {

        // Update role
        userClient.updateUserRole(profile.getUserId(), "ROLE_LEARNER");

        return repo.save(profile);
    }

    public LearnerProfileDTO getProfile(Long userId) {

        LearnerProfile profile = repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        UsersDTO user = userClient.findUserById(userId);

        List<SkillDTO> skills = skillClient.findSkillsByUserId(userId);

        int age = Period.between(profile.getDob(), LocalDate.now()).getYears();

        return new LearnerProfileDTO(
                profile.getId(),
                userId,
                user.getName(),
                user.getEmail(),
                profile.getPhone(),
                profile.getProfilePic(),
                profile.getDob(),
                age,
                profile.getLocation(),
                profile.getBio(),
                skills
        );
    }

    public SkillDTO addSkill(Long userId, SkillDTO skill) {
        skill.setUser_id(userId);
        return skillClient.saveSkill(skill);
    }
}
