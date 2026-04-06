package com.lpu.LearnerService.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lpu.LearnerService.client.SkillServiceClient;
import com.lpu.LearnerService.client.UserServiceClient;
import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.dto.SkillDTO;
import com.lpu.LearnerService.dto.UsersDTO;
import com.lpu.LearnerService.entity.LearnerProfile;
import com.lpu.LearnerService.entity.LearnerSkill;
import com.lpu.LearnerService.mapper.LearnerMapper;
import com.lpu.LearnerService.repository.LearnerRepository;
import com.lpu.LearnerService.repository.LearnerSkillRepository;
import com.lpu.LearnerService.service.LearnerService;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
public class LearnerServiceImpl implements LearnerService {

	private static final Logger logger = LoggerFactory.getLogger(LearnerServiceImpl.class);

    private final LearnerRepository repo;
    private final LearnerSkillRepository learnerSkillRepository;
    private final UserServiceClient userClient;
    private final SkillServiceClient skillClient;
    private final MeterRegistry meterRegistry;
    private final LearnerMapper learnerMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    @CachePut(value = "learnerProfile", key = "#profileDTO.userId")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCreateProfile")
    @Retry(name = "userService")
    public LearnerProfileDTO createProfile(LearnerProfileDTO profileDTO) {
		logger.info("Initializing profile creation for user: {}", profileDTO.getUserId());
        
        LearnerProfile profile = learnerMapper.toEntity(profileDTO);

        // Update role
        userClient.updateUserRole(profile.getUserId(), "ROLE_LEARNER");
		logger.info("Role updated to ROLE_LEARNER for user: {}", profile.getUserId());
        LearnerProfile savedProfile = repo.save(profile);
		logger.info("Learner profile saved successfully for user: {}", profile.getUserId());
        
        meterRegistry.counter("learner.registrations.total").increment();
		return learnerMapper.toDto(savedProfile);
    }

    public LearnerProfileDTO fallbackCreateProfile(LearnerProfileDTO profileDTO, Exception ex) {
        logger.error("Fallback triggered for createProfile for UserID: {} due to: {}", profileDTO.getUserId(), ex.getMessage());
        LearnerProfile profile = learnerMapper.toEntity(profileDTO);
        LearnerProfile saved = repo.save(profile);
        return learnerMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('LEARNER', 'USER')")
    @CachePut(value = "learnerProfile", key = "#userId")
    public LearnerProfileDTO updateProfile(Long userId, LearnerProfileDTO profileDTO) {
        logger.info("Updating learner profile for user: {}", userId);
        LearnerProfile profile = repo.findByUserId(userId).orElseGet(() -> {
            logger.info("No existing profile for user {}, creating new one", userId);
            LearnerProfile newProfile = new LearnerProfile();
            newProfile.setUserId(userId);
            return newProfile;
        });
        if (profileDTO.getPhone() != null) profile.setPhone(profileDTO.getPhone());
        if (profileDTO.getLocation() != null) profile.setLocation(profileDTO.getLocation());
        if (profileDTO.getBio() != null) profile.setBio(profileDTO.getBio());
        if (profileDTO.getDob() != null) profile.setDob(profileDTO.getDob());
        LearnerProfile saved = repo.save(profile);
        logger.info("Learner profile updated for user: {}", userId);
        return learnerMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    @CacheEvict(value = "learnerProfile", key = "#userId")
    public LearnerSkill addSkill(Long userId, Long skillId) {
		logger.info("Adding skill mapping: UserID {} -> SkillID {}", userId, skillId);
        LearnerSkill learnerSkill = new LearnerSkill();
        learnerSkill.setUserId(userId);
        learnerSkill.setSkillId(skillId);
        LearnerSkill savedMapping = learnerSkillRepository.save(learnerSkill);
		logger.info("Skill mapping saved with ID: {}", savedMapping.getId());
		return savedMapping;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    @CacheEvict(value = "learnerProfile", key = "#userId")
    public void removeSkill(Long userId, Long skillId) {
		logger.info("Removing skill mapping: UserID {} -> SkillID {}", userId, skillId);
        learnerSkillRepository.deleteByUserIdAndSkillId(userId, skillId);
		logger.info("Skill mapping deleted for UserID {} and SkillID {}", userId, skillId);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@Cacheable(value = "learnerProfileDTO", key = "#userId")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetProfile")
    @Retry(name = "userService")
	public LearnerProfileDTO getProfile(Long userId) {
		logger.info("Assembling LearnerProfileDTO for UserID: {}", userId);

		LearnerProfile profile = repo.findByUserId(userId).orElseThrow(() -> {
			logger.error("Profile query failed: UserID {} not found", userId);
			return new RuntimeException("Profile not found");
		});

		logger.debug("Profile found in DB for user {}. Fetching user details from UserServiceClient...", userId);
		UsersDTO user = userClient.findUserById(userId);

        List<Long> skillIds = learnerSkillRepository.findByUserId(userId)
                .stream()
                .map(LearnerSkill::getSkillId)
                .collect(Collectors.toList());

		List<SkillDTO> skills = List.of();
        if (!skillIds.isEmpty()) {
			logger.info("Found {} skill mappings for user {}. Fetching skill details from SkillServiceClient...", skillIds.size(), userId);
            skills = skillClient.findSkillsByIds(skillIds);
        }

		int age = profile.getDob() != null ? Period.between(profile.getDob(), LocalDate.now()).getYears() : 0;

		return new LearnerProfileDTO(
				profile.getId(), 
				userId, 
				user.getName(), 
				user.getEmail(), 
				profile.getPhone(),
				profile.getDob(), 
				age, 
				profile.getLocation(), 
				profile.getBio(), 
				skills
			);
	}

    public LearnerProfileDTO fallbackGetProfile(Long userId, Exception ex) {
        logger.error("Fallback triggered for getProfile for UserID: {} due to: {}", userId, ex.getMessage());
        LearnerProfile profile = repo.findByUserId(userId).orElse(new LearnerProfile());
        return new LearnerProfileDTO(profile.getId(), userId, "Unavailable", "Unavailable", profile.getPhone(), profile.getDob(), 0, profile.getLocation(), profile.getBio(), List.of());
    }
}
