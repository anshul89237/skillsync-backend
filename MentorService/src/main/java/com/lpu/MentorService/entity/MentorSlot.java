package com.lpu.MentorService.entity;

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
@Table(name = "mentor_slots", indexes = {
    @Index(name = "idx_mentorslot_mentor_id", columnList = "mentorId"),
    @Index(name = "idx_mentorslot_start_time", columnList = "startTime"),
    @Index(name = "idx_mentorslot_end_time", columnList = "endTime")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MentorSlot extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long mentorId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Integer currentLearners = 0;
	private Integer maxLearners = 100;
}
