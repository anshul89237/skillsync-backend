package com.lpu.MentorService.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.MentorDTO;
import com.lpu.MentorService.dto.MentorProfileDTO;
import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.java.common_security.config.SecurityUtils;
import com.lpu.MentorService.service.MentorService;
import com.lpu.java.common_security.dto.ApiResponse;

import lombok.RequiredArgsConstructor;


@RequestMapping("/api/v1/mentors")
@RestController
@RequiredArgsConstructor
public class MentorController {

	private static final Logger logger = LoggerFactory.getLogger(MentorController.class);

    private final MentorService mentorService;
    private final UserServiceClient client;

	@PostMapping("/apply")
	public ResponseEntity<ApiResponse<MentorDTO>> saveMentor(@RequestBody MentorDTO mentorDTO) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		logger.info("Mentor application received for user ID: {}", currentUserId);
		// Zero Trust: Extract user ID directly from current authenticated JWT context
		mentorDTO.setUserId(currentUserId);
 
		return ResponseEntity.ok(ApiResponse.success("Application submitted successfully, pending approval", mentorService.saveMentor(mentorDTO)));
	}

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<List<MentorDTO>>> getApplications() {
        logger.info("Request to fetch all pending mentor applications");
        return ResponseEntity.ok(ApiResponse.success("Pending applications fetched successfully", mentorService.getApplications()));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<MentorDTO>> approveApplication(@PathVariable("id") Long id) {
        logger.info("Request to approve mentor application ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Mentor application approved", mentorService.approveApplication(id)));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<MentorDTO>> rejectApplication(@PathVariable("id") Long id) {
        logger.info("Request to reject mentor application ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Mentor application rejected", mentorService.rejectApplication(id)));
    }
 
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<MentorDTO>> findMentorById(@PathVariable("id") Long id) {
		logger.info("Request to fetch mentor by ID: {}", id);
		return ResponseEntity.ok(ApiResponse.success("Mentor fetched successfully", mentorService.findMentorById(id)));
	}
 
	@GetMapping
	public ResponseEntity<ApiResponse<List<MentorDTO>>> findAllMentors() {
		logger.info("Request to fetch all mentors");
		return ResponseEntity.ok(ApiResponse.success("Mentors fetched successfully", mentorService.findMentors()));
	}
 
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<MentorDTO>> updateMentorById(@PathVariable("id") Long id, @RequestBody MentorDTO mentorDTO) {
		logger.info("Request to update mentor ID: {}", id);
		// 🔐 Zero Trust: Extract user ID directly from current authenticated JWT context
		mentorDTO.setUserId(SecurityUtils.getCurrentUserId());
 
		return ResponseEntity.ok(ApiResponse.success("Mentor updated successfully", mentorService.updateMentorById(id, mentorDTO)));
	}
 
	@PutMapping("/users/{user_id}")
	public ResponseEntity<ApiResponse<UsersDTO>> updateUserDTOById(@PathVariable("user_id") Long user_id, @RequestBody UsersDTO user) {
		logger.info("Proxy request to update user profile for user ID: {}", user_id);
		ApiResponse<UsersDTO> userResponse = client.findUserById(user_id);
		UsersDTO u = userResponse != null ? userResponse.getData() : null;
 
		if (u == null) {
			logger.error("User update failed: User ID {} not found", user_id);
			throw new RuntimeException("No user found with user id : " + user_id + ". Create account");
		}
 
		ApiResponse<UsersDTO> updatedResponse = client.updateUserById(user_id, user);
		return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", updatedResponse != null ? updatedResponse.getData() : null));
	}
 
	@GetMapping("/users/{user_id}")
	public ResponseEntity<ApiResponse<MentorDTO>> findMentorByUserId(@PathVariable("user_id") Long user_id) {
		logger.info("Request to fetch mentor by User ID: {}", user_id);
		return ResponseEntity.ok(ApiResponse.success("Mentor fetched successfully", mentorService.findMentorByUserId(user_id)));
	}
 
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteMentorById(@PathVariable("id") Long id) {
		logger.info("Request to delete mentor ID: {}", id);
		mentorService.deleteMentorById(id);
        return ResponseEntity.ok(ApiResponse.success("Mentor deleted successfully", null));
	}
 
	@PutMapping("/{id}/rating")
	public ResponseEntity<ApiResponse<String>> updateRating(@PathVariable Long id, @RequestParam Double rating) {
		logger.info("Request to update rating for mentor ID: {} to {}", id, rating);
		mentorService.updateRating(id, rating);
		return ResponseEntity.ok(ApiResponse.success("Rating updated", "Success"));
	}
 
	@GetMapping("/profile/{userId}")
	public ResponseEntity<ApiResponse<MentorProfileDTO>> getProfile(@PathVariable Long userId) {
		logger.info("Request to fetch full mentor profile for user ID: {}", userId);
		return ResponseEntity.ok(ApiResponse.success("Mentor profile fetched successfully", mentorService.getMentorProfile(userId)));
	}
 
	@PostMapping("/self/skills")
	public ResponseEntity<ApiResponse<Void>> addSkill(@RequestParam Long skillId) {
		Long userId = SecurityUtils.getCurrentUserId();
		logger.info("Request to add skill ID {} to self (User ID: {})", skillId, userId);
		// 🔐 Zero Trust: Extract user ID directly from current authenticated JWT context
		mentorService.addSkill(userId, skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill added successfully", null));
	}
 
    @DeleteMapping("/self/skills")
    public ResponseEntity<ApiResponse<Void>> removeSkill(@RequestParam Long skillId) {
		Long userId = SecurityUtils.getCurrentUserId();
		logger.info("Request to remove skill ID {} from self (User ID: {})", skillId, userId);
        // 🔐 Zero Trust: Extract user ID directly from current authenticated JWT context
        mentorService.removeSkill(userId, skillId);
        return ResponseEntity.ok(ApiResponse.success("Skill removed successfully", null));
    }
}