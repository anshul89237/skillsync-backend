package com.lpu.MentorService.entity;

import java.io.Serializable;

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
@Table(name = "mentor_skills", indexes = {
    @Index(name = "idx_mentorskill_mentor_id", columnList = "mentorId"),
    @Index(name = "idx_mentorskill_skill_id", columnList = "skillId"),
    @Index(name = "idx_mentorskill_user_id", columnList = "userId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MentorSkill extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Long mentorId;
	private Long userId;
	private Long skillId;
}
