package com.lpu.SessionService.dto;

import java.time.LocalDateTime;

public class SessionEvent {
    private Long sessionId;
    private Long userId;
    private Long mentorId;
    private String status;
    private LocalDateTime session_date;
    
	public SessionEvent() {
		super();
	}
	
	public SessionEvent(Long sessionId, Long userId, Long mentorId, String status, LocalDateTime session_date) {
		super();
		this.sessionId = sessionId;
		this.userId = userId;
		this.mentorId = mentorId;
		this.status = status;
		this.session_date = session_date;
	}
	
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
