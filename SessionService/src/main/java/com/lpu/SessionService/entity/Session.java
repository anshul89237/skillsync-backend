package com.lpu.SessionService.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.lpu.java.common_security.audit.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sessions", indexes = {
    @Index(name = "idx_session_mentor_id", columnList = "mentorId"),
    @Index(name = "idx_session_user_id", columnList = "userId"),
    @Index(name = "idx_session_date", columnList = "sessionDate"),
    @Index(name = "idx_session_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Session extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long mentorId;
	private Long userId;
	private Long slotId;
	private LocalDateTime sessionDate;
	private String status;
}
