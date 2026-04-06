package com.lpu.SessionService.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.lpu.java.common_security.config.SecurityUtils;
import com.lpu.SessionService.dto.SessionDTO;
import com.lpu.SessionService.service.SessionService;
import lombok.RequiredArgsConstructor;

@RequestMapping({ "/sessions", "/api/v1/sessions" })
@RestController
@RequiredArgsConstructor
public class SessionController {

	private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

	private final SessionService sessionService;

	@PostMapping({ "", "/book" })
	public SessionDTO createsSession(@RequestBody SessionDTO sessionDTO) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		logger.info("Session creation request received for User ID: {} and Mentor ID: {}", currentUserId, sessionDTO.getMentorId());
		// 🔐 Zero Trust: Extract learner ID directly from current authenticated JWT context
		sessionDTO.setUserId(currentUserId);
		
		return sessionService.createSession(sessionDTO);
	}
	
	@GetMapping("/{id}")
	public SessionDTO findSessionById(@PathVariable("id") Long id) {
		logger.info("Request to fetch session by ID: {}", id);
		return sessionService.findSessionById(id);
	}
	
	@GetMapping
	public List<SessionDTO> findAllSessions() {
		logger.info("Request to fetch all sessions");
		return sessionService.findAllSessions();
	}

	@GetMapping("/learner/{userId}")
	public List<SessionDTO> findSessionsByLearnerId(@PathVariable("userId") Long userId) {
		logger.info("Request to fetch sessions for learner user ID: {}", userId);
		return sessionService.findSessionsByLearnerId(userId);
	}

	@GetMapping("/mentor/{mentorId}")
	public List<SessionDTO> findSessionsByMentorId(@PathVariable("mentorId") Long mentorId) {
		logger.info("Request to fetch sessions for mentor ID: {}", mentorId);
		return sessionService.findSessionsByMentorId(mentorId);
	}
	
	@DeleteMapping("/{id}")
	public void deleteSessionById(@PathVariable("id") Long id) {
		logger.info("Request to delete session ID: {}", id);
		sessionService.deleteSessionById(id);
		logger.info("Session ID {} deleted successfully", id);
	}
	
	@PutMapping("/{id}/{status}")
    public ResponseEntity<SessionDTO> updateStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status
    ) {
		logger.info("Request to update session ID {} to status: {}", id, status);
        if ("ACCEPTED".equalsIgnoreCase(status)) {
			logger.info("Session ID {} accepted. Transitioning status to SCHEDULED", id);
            return ResponseEntity.ok(sessionService.updateStatus(id, "SCHEDULED"));
        }
        return ResponseEntity.ok(sessionService.updateStatus(id, status));
    }
}
