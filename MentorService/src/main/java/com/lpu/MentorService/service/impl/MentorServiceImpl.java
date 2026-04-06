package com.lpu.MentorService.service.impl;

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

import com.lpu.MentorService.client.SkillServiceClient;
import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.MentorDTO;
import com.lpu.MentorService.dto.MentorProfileDTO;
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.exception.MentorNotFoundException;
import com.lpu.MentorService.mapper.MentorMapper;
import com.lpu.MentorService.repository.MentorRepository;
import com.lpu.MentorService.repository.MentorSkillRepository;
import com.lpu.MentorService.service.MentorService;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

	private static final Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    private final MentorRepository repository;
    private final MentorSkillRepository mentorSkillRepository;
    private final UserServiceClient userClient;
    private final SkillServiceClient skillClient;
    private final MeterRegistry meterRegistry;
    private final MentorMapper mentorMapper;

    @Override
    @PreAuthorize("hasRole('USER')")
    @CachePut(value = "mentor", key = "#result.id")
    @CacheEvict(value = "mentors", allEntries = true)
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackSaveMentor")
    @Retry(name = "userService")
    public MentorDTO saveMentor(MentorDTO mentorDTO) {
		logger.info("Filing mentor application for User ID: {}", mentorDTO.getUserId());
        
        Mentor mentor = mentorMapper.toEntity(mentorDTO);
        mentor.setStatus("PENDING");
        Mentor savedMentor = repository.save(mentor);
		logger.info("Mentor application saved with ID: {} and status: PENDING", savedMentor.getId());
        
        meterRegistry.counter("mentor.applications.total").increment();
		return mentorMapper.toDto(savedMentor);
    }

    public MentorDTO fallbackSaveMentor(MentorDTO mentorDTO, Exception ex) {
        logger.error("Fallback triggered for saveMentor for UserID: {} due to: {}", mentorDTO.getUserId(), ex.getMessage());
        Mentor mentor = mentorMapper.toEntity(mentorDTO);
        mentor.setStatus("PENDING");
        Mentor saved = repository.save(mentor);
        return mentorMapper.toDto(saved);
    }

    @Override
    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN')")
    @CachePut(value = "mentor", key = "#id")
    @CacheEvict(value = "mentors", allEntries = true)
    public MentorDTO updateMentorById(Long id, MentorDTO mentorDTO) {
		logger.info("Updating details for mentor ID: {}", id);
        Mentor m = repository.findById(id).orElseThrow(() -> {
			logger.error("Update failed: Mentor ID {} not found", id);
			return new MentorNotFoundException("No mentor found with id: " + id);
		});
        m.setBio(mentorDTO.getBio());
        m.setExperience(mentorDTO.getExperience());
        m.setHourlyRate(mentorDTO.getHourlyRate());
        m.setPhone(mentorDTO.getPhone());
        m.setDob(mentorDTO.getDob());
        m.setLocation(mentorDTO.getLocation());
        Mentor updated = repository.save(m);
		logger.info("Successfully updated mentor details for ID: {}", id);
		return mentorMapper.toDto(updated);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    @CacheEvict(value = "mentor", key = "#id", allEntries = true)
    public void deleteMentorById(Long id) {
		logger.info("Deleting mentor application/profile for ID: {}", id);
        repository.findById(id).orElseThrow(() -> {
			logger.error("Deletion failed: Mentor ID {} not found", id);
			return new MentorNotFoundException("No mentor found with id: " + id);
		});
        repository.deleteById(id);
		logger.info("Successfully deleted mentor ID: {}", id);
    }

    @Override
    @CachePut(value = "mentor", key = "#mentorId")
    @CacheEvict(value = "mentors", allEntries = true)
    public MentorDTO updateRating(Long mentorId, Double rating) {
		logger.info("Updating rating for mentor ID: {} to {}", mentorId, rating);
        Mentor mentor = repository.findById(mentorId).orElseThrow(() -> {
			logger.error("Rating update failed: Mentor ID {} not found", mentorId);
			return new MentorNotFoundException("No mentor found with id: " + mentorId);
		});
        mentor.setRating(rating);
        Mentor updated = repository.save(mentor);
		logger.info("Successfully updated rating for mentor ID: {}", mentorId);
		return mentorMapper.toDto(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MENTOR')")
    @CacheEvict(value = "mentor", allEntries = true)
    public MentorSkill addSkill(Long userId, Long skillId) {
		logger.info("Adding skill mapping for Mentor (User ID: {}) and Skill ID: {}", userId, skillId);
        MentorSkill mentorSkill = new MentorSkill();
        mentorSkill.setUserId(userId);
        mentorSkill.setSkillId(skillId);
        MentorSkill savedMapping = mentorSkillRepository.save(mentorSkill);
		logger.info("Skill mapping saved successfully");
		return savedMapping;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MENTOR')")
    @CacheEvict(value = "mentor", allEntries = true)
    public void removeSkill(Long userId, Long skillId) {
		logger.info("Removing skill mapping for Mentor (User ID: {}) and Skill ID: {}", userId, skillId);
        mentorSkillRepository.deleteByUserIdAndSkillId(userId, skillId);
		logger.info("Skill mapping deleted successfully");
    }

    @Override
    @Cacheable(value = "mentor", key = "#id", unless = "#result == null")
    public MentorDTO findMentorById(Long id) {
		logger.info("Fetching mentor details for ID: {}", id);
        Mentor mentor = repository.findById(id).orElseThrow(() -> {
			logger.error("Mentor lookup failed: ID {} not found", id);
			return new MentorNotFoundException("No mentor found with id: " + id);
		});
		return mentorMapper.toDto(mentor);
    }

    @Override
    @Cacheable(value = "mentors", unless = "#result == null || #result.isEmpty()")
    public List<MentorDTO> findMentors() {
		logger.info("Fetching all active mentors (status: APPROVED)");
        List<Mentor> mentors = repository.findByStatus("APPROVED");
		logger.info("Found {} approved mentors", mentors.size());
		return mentorMapper.toDtoList(mentors);
    }

    @Override
    public MentorDTO findMentorByUserId(Long userId) {
		logger.info("Fetching mentor profile by User ID: {}", userId);
        Mentor mentor = repository.findByUserId(userId).orElse(null);
		if (mentor == null) {
			logger.warn("Profile lookup: No mentor profile exists for User ID: {}", userId);
			return null;
		}
		return mentorMapper.toDto(mentor);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<MentorDTO> getApplications() {
		logger.info("Admin request: Fetching all pending mentor applications");
        List<Mentor> applications = repository.findByStatus("PENDING");
		logger.info("Found {} pending applications", applications.size());
		return mentorMapper.toDtoList(applications);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "mentors", allEntries = true)
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackApproveApplication")
    @Retry(name = "userService")
    public MentorDTO approveApplication(Long id) {
		logger.info("Approving mentor application for ID: {}", id);
        Mentor mentor = repository.findById(id).orElseThrow(() -> {
			logger.error("Approval failed: Mentor application ID {} not found", id);
			return new MentorNotFoundException("No mentor found with id: " + id);
		});
        
        mentor.setStatus("APPROVED");
        userClient.updateUserRole(mentor.getUserId(), "ROLE_MENTOR");
		logger.info("User role for User ID {} updated to ROLE_MENTOR. Application approved.", mentor.getUserId());
        
        Mentor saved = repository.save(mentor);
		return mentorMapper.toDto(saved);
    }

    public MentorDTO fallbackApproveApplication(Long id, Exception ex) {
        logger.error("Fallback triggered for approveApplication for ID: {} due to: {}", id, ex.getMessage());
        Mentor mentor = repository.findById(id).orElseThrow(() -> new MentorNotFoundException("No mentor found with id: " + id));
        mentor.setStatus("APPROVED_ROLE_PENDING_SYNC");
        return mentorMapper.toDto(repository.save(mentor));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public MentorDTO rejectApplication(Long id) {
		logger.info("Rejecting mentor application for ID: {}", id);
        Mentor mentor = repository.findById(id).orElseThrow(() -> {
			logger.error("Rejection failed: Mentor application ID {} not found", id);
			return new MentorNotFoundException("No mentor found with id: " + id);
		});
        mentor.setStatus("REJECTED");
        Mentor saved = repository.save(mentor);
		logger.info("Application ID {} status set to REJECTED", id);
		return mentorMapper.toDto(saved);
    }

    @Override
    public MentorProfileDTO getMentorProfile(Long userId) {
		logger.info("Assembling comprehensive MentorProfileDTO for user ID: {}", userId);
        Mentor mentor = repository.findByUserId(userId).orElseThrow(() -> {
			logger.error("Profile assembly failed: No mentor found for User ID: {}", userId);
            throw new MentorNotFoundException("No mentor found with user id: " + userId);
        });

		logger.debug("Fetching user data from UserServiceClient for User ID: {}", userId);
        UsersDTO user = userClient.findUserById(userId).getData();

        List<Long> skillIds = mentorSkillRepository.findByUserId(userId)
                .stream()
                .map(MentorSkill::getSkillId)
                .collect(Collectors.toList());

		List<SkillDTO> skills = List.of();
        if (!skillIds.isEmpty()) {
			logger.debug("Fetching skill details from SkillServiceClient for User ID: {}", userId);
            skills = skillClient.findSkillsByIds(skillIds).getData();
        }

		logger.info("Successfully assembled profile for mentor user ID: {}", userId);
        return new MentorProfileDTO(
            mentor.getId(),
            userId,
            user.getName(),
            user.getEmail(),
            mentor.getPhone(),
            mentor.getDob(),
            0, // age
            mentor.getLocation(),
            mentor.getBio(),
            mentor.getExperience(),
            mentor.getHourlyRate(),
            mentor.getRating(),
            skills
        );
    }
}
