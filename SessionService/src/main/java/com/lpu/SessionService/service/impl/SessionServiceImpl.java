package com.lpu.SessionService.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lpu.SessionService.dto.SessionDTO;
import com.lpu.SessionService.dto.SessionEvent;
import com.lpu.SessionService.entity.Session;
import com.lpu.SessionService.exception.SessionNotFoundException;
import com.lpu.SessionService.mapper.SessionMapper;
import com.lpu.SessionService.producer.SessionEventProducer;
import com.lpu.SessionService.repository.SessionRepository;
import com.lpu.SessionService.service.SessionService;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

import com.lpu.SessionService.client.MentorServiceClient;
import com.lpu.SessionService.client.MentorSlotServiceClient;
import com.lpu.SessionService.dto.MentorDTO;
import com.lpu.SessionService.dto.MentorSlotDTO;
import com.lpu.java.common_security.dto.ApiResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

	private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final SessionRepository repository;
    private final SessionEventProducer producer;
    private final MentorServiceClient mentorClient;
    private final MentorSlotServiceClient mentorSlotClient;
    private final MeterRegistry meterRegistry;
    private final SessionMapper sessionMapper;

    @Override
    @PreAuthorize("#sessionDTO.userId == authentication.principal.id")
    @CachePut(value = "sessions", key = "#result.id")
    @CircuitBreaker(name = "mentorService", fallbackMethod = "fallbackSessionCreation")
    @Retry(name = "mentorService")
    public SessionDTO createSession(SessionDTO sessionDTO) {
		logger.info("Session creation request received for User ID: {} and Mentor ID: {}", sessionDTO.getUserId(), sessionDTO.getMentorId());

        Session session = sessionMapper.toEntity(sessionDTO);

		// Validate Mentor
		logger.debug("Validating mentor ID: {} via MentorServiceClient...", session.getMentorId());
		ApiResponse<MentorDTO> mentorResponse = mentorClient.findMentorById(session.getMentorId());
		MentorDTO mentor = mentorResponse != null ? mentorResponse.getData() : null;
		if (mentor == null) {
			logger.error("Session creation failed: Mentor ID {} not found", session.getMentorId());
			throw new RuntimeException("No mentor found with mentor id : " + session.getMentorId() + ". Create mentor account");
		}
		
		// Handle Slot Booking
		if (session.getSlotId() != null) {
			try {
				logger.info("Attempting to book slot ID: {} for session", session.getSlotId());
                MentorSlotDTO slotInfo = mentorSlotClient.getSlot(session.getSlotId());
                if (slotInfo != null && slotInfo.getStartTime() != null) {
                    session.setSessionDate(slotInfo.getStartTime());
					logger.debug("Set session date from slot: {}", slotInfo.getStartTime());
                }
				mentorSlotClient.incrementSlot(session.getSlotId());
				logger.info("Slot ID {} successfully booked/incremented", session.getSlotId());
			} catch (Exception e) {
				logger.error("Slot booking failed for Slot ID {}: {}", session.getSlotId(), e.getMessage());
				throw new RuntimeException("Failed to book slot. It might be full or invalid.");
			}
		}

		// Determine Status
		logger.debug("Checking previous sessions for User {} and Mentor {}...", session.getUserId(), session.getMentorId());
		List<Session> previousSessions = repository.findByUserIdAndMentorId(session.getUserId(), session.getMentorId());
		
		if (previousSessions.isEmpty()) {
			session.setStatus("PENDING_APPROVAL"); // First session is free, needs mentor approval
			logger.info("First session detected. Status set to PENDING_APPROVAL for user {}", session.getUserId());
		} else {
			session.setStatus("PAYMENT_PENDING"); // Subsequent sessions require payment
			logger.info("Subsequent session detected. Status set to PAYMENT_PENDING for user {}", session.getUserId());
		}

        session.setCreatedAt(LocalDateTime.now());
		logger.info("Saving session with status: {}", session.getStatus());
		Session savedSession = repository.save(session);
        
        meterRegistry.counter("sessions.created.total", "status", savedSession.getStatus()).increment();

		// Send event for notification
		SessionEvent event = new SessionEvent(
				savedSession.getId(), 
				savedSession.getUserId(), 
				savedSession.getMentorId(), 
				savedSession.getStatus(), 
				savedSession.getSessionDate());
		producer.sendSessionEvent(event);

		return sessionMapper.toDto(savedSession);
    }

    // Fallback for createSession
    public SessionDTO fallbackSessionCreation(SessionDTO sessionDTO, Exception ex) {
        logger.error("Fallback triggered for createSession due to: {}", ex.getMessage());
		sessionDTO.setSessionDate((LocalDateTime) null);
        sessionDTO.setStatus("SERVICE_UNAVAILABLE_FALLBACK");
        return sessionDTO;
    }

    @Override
    @CachePut(value = "sessions", key = "#id")
    public SessionDTO updateSessionDateById(Long id, SessionDTO sessionDTO) {
		logger.info("Updating date for session ID: {}", id);
		Session s = repository.findById(id).orElseThrow(() -> {
			logger.error("Update failed: Session ID {} not found", id);
			return new SessionNotFoundException("Session not found with id: " + id);
		});
		s.setSessionDate(sessionDTO.getSessionDate());
		Session updatedSession = repository.save(s);
		logger.info("Successfully updated date for session ID: {}", id);
		return sessionMapper.toDto(updatedSession);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','LEARNER')")
    @CacheEvict(value = "sessions", key = "#id")
    public void deleteSessionById(Long id) {
		logger.info("Deleting session ID: {}", id);
		repository.findById(id).orElseThrow(() -> {
			logger.error("Deletion failed: Session ID {} not found", id);
			return new SessionNotFoundException("Session not found with id: " + id);
		});
		repository.deleteById(id);
		logger.info("Successfully deleted session ID: {}", id);
    }

    @Override
    @PreAuthorize("hasAnyRole('MENTOR')")
    @CachePut(value = "sessions", key = "#id")
    public SessionDTO updateStatus(Long id, String status) {
		logger.info("Updating status for session ID: {} to {}", id, status);
		Session session = repository.findById(id).orElseThrow(() -> {
			logger.error("Status update failed: Session ID {} not found", id);
			return new SessionNotFoundException("Session not found with id: " + id);
		});
		session.setStatus(status);
		repository.save(session);
		logger.info("Successfully updated status to {} for session ID: {}", status, id);

		SessionEvent event = new SessionEvent(
				session.getId(), 
				session.getUserId(), 
				session.getMentorId(), 
				session.getStatus(), 
				session.getSessionDate());
		
		logger.debug("Producing SessionEvent for Async Processing: {}", event);
		producer.sendSessionEvent(event);
		logger.info("SessionEvent sent to message broker for session ID: {}", id);
		return sessionMapper.toDto(session);
    }

    @Override
    @Cacheable(value = "sessions", key = "#id", unless = "#result == null")
    public SessionDTO findSessionById(Long id) {
		logger.info("Fetching session details by ID: {}", id);
		Session session = repository.findById(id).orElseThrow(() -> {
			logger.error("Session lookup failed: ID {} not found", id);
			return new SessionNotFoundException("Session not found with id: " + id);
		});
        return sessionMapper.toDto(session);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Cacheable(value = "allSessions", unless = "#result == null || #result.isEmpty()")
    public List<SessionDTO> findAllSessions() {
		logger.info("Admin request: Fetching all sessions in the system");
		List<Session> sessions = repository.findAll();
		logger.info("Found {} total sessions", sessions.size());
		return sessionMapper.toDtoList(sessions);
    }

    @Override
    @Cacheable(value = "sessionsByLearner", key = "#userId", unless = "#result == null || #result.isEmpty()")
    public List<SessionDTO> findSessionsByLearnerId(Long userId) {
		logger.info("Fetching sessions for Learner User ID: {}", userId);
		List<Session> sessions = repository.findByUserId(userId);
		logger.info("Found {} sessions for learner User ID: {}", sessions.size(), userId);
		return sessionMapper.toDtoList(sessions);
    }

    @Override
    @Cacheable(value = "sessionsByMentor", key = "#mentorId", unless = "#result == null || #result.isEmpty()")
    public List<SessionDTO> findSessionsByMentorId(Long mentorId) {
		logger.info("Fetching sessions for Mentor ID: {}", mentorId);
		List<Session> sessions = repository.findByMentorId(mentorId);
		logger.info("Found {} sessions for mentor ID: {}", sessions.size(), mentorId);
		return sessionMapper.toDtoList(sessions);
    }

    @Override
    public List<SessionDTO> findSessionsByLearnerAndMentor(Long userId, Long mentorId) {
		logger.debug("Checking session existence for Learner User ID: {} and Mentor ID: {}", userId, mentorId);
		return sessionMapper.toDtoList(repository.findByUserIdAndMentorId(userId, mentorId));
    }
}
