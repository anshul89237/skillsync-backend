package com.lpu.SessionService.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {
    private Long id;
    private Long mentorId;
    private Long userId;
    private Long learnerId;
    private Long slotId;
    private LocalDateTime sessionDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String topic;
    private String status;
    private LocalDateTime createdAt;

    public void setLearnerId(Long learnerId) {
        this.learnerId = learnerId;
        if (this.userId == null) {
            this.userId = learnerId;
        }
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        if (this.learnerId == null) {
            this.learnerId = userId;
        }
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        if (this.startTime != null && durationMinutes != null) {
            this.endTime = this.startTime.plusMinutes(durationMinutes);
        }
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = parseDateTime(sessionDate);
        if (this.startTime == null) {
            this.startTime = this.sessionDate;
        }
        if (this.endTime == null && this.durationMinutes != null && this.sessionDate != null) {
            this.endTime = this.sessionDate.plusMinutes(this.durationMinutes);
        }
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
        if (this.startTime == null) {
            this.startTime = sessionDate;
        }
        if (this.endTime == null && this.durationMinutes != null && sessionDate != null) {
            this.endTime = sessionDate.plusMinutes(this.durationMinutes);
        }
    }

    public void setStartTime(String startTime) {
        this.startTime = parseDateTime(startTime);
        if (this.sessionDate == null) {
            this.sessionDate = this.startTime;
        }
        if (this.endTime == null && this.durationMinutes != null && this.startTime != null) {
            this.endTime = this.startTime.plusMinutes(this.durationMinutes);
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        if (this.sessionDate == null) {
            this.sessionDate = startTime;
        }
        if (this.endTime == null && this.durationMinutes != null && startTime != null) {
            this.endTime = startTime.plusMinutes(this.durationMinutes);
        }
    }

    public void setEndTime(String endTime) {
        this.endTime = parseDateTime(endTime);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private static LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ex) {
            return OffsetDateTime.parse(value).toLocalDateTime();
        }
    }
}
