package com.lpu.NotificationService.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessionDTO {
    private Long id;
    private Long mentorId;
    private Long userId;
    private Long slotId;
    private LocalDateTime session_date;
    private String status;
}
