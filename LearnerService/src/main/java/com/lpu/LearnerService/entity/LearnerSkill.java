package com.lpu.LearnerService.entity;
 
import java.io.Serializable;

import com.lpu.java.common_security.audit.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
 
@Entity
@Table(name = "learner_skills", indexes = {
    @Index(name = "idx_learnerskill_user_id", columnList = "userId"),
    @Index(name = "idx_learnerskill_skill_id", columnList = "skillId")
})
@Getter
@Setter
public class LearnerSkill extends BaseEntity implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private Long userId;
    private Long skillId;
}
