package com.lpu.LearnerService.entity;

import java.io.Serializable;
import java.time.LocalDate;

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
@Table(name = "learner_profile", indexes = {
    @Index(name = "idx_learner_user_id", columnList = "userId")
})
@Getter
@Setter
public class LearnerProfile extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String phone;
    private LocalDate dob;
    private String location;
    private String bio;
}