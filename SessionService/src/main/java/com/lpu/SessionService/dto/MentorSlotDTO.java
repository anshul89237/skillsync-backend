package com.lpu.SessionService.dto;

import java.time.LocalDateTime;

public class MentorSlotDTO {

    private Long id;
    private Long mentorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMentorId() {
        return mentorId;
    }
    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
