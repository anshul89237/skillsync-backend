package com.lpu.NotificationService.dto;

import java.time.LocalDateTime;

public class SessionEvent {
    private Long sessionId;
    private Long learnerId;
    private Long mentorId;
    private String status; // ACCEPTED, REJECTED, CANCELLED
    private LocalDateTime session_date;
    
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Long getLearnerId() {
		return learnerId;
	}
	public void setLearnerId(Long learnerId) {
		this.learnerId = learnerId;
	}
	public Long getMentorId() {
		return mentorId;
	}
	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getSession_date() {
		return session_date;
	}
	public void setSession_date(LocalDateTime session_date) {
		this.session_date = session_date;
	}
	
	
}
