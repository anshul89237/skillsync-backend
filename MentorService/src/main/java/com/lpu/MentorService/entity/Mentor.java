package com.lpu.MentorService.entity;

import java.io.Serializable;
import java.time.LocalDate;

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
@Table(name = "mentors", indexes = {
    @Index(name = "idx_mentor_user_id", columnList = "userId"),
    @Index(name = "idx_mentor_rating", columnList = "rating"),
    @Index(name = "idx_mentor_experience", columnList = "experience"),
    @Index(name = "idx_mentor_hourly_rate", columnList = "hourlyRate"),
    @Index(name = "idx_mentor_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Mentor extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String bio;
    private Double experience;
    private Double rating;
    private Double hourlyRate;

    private String phone;
    private LocalDate dob;
    private String location;

    private String status = "PENDING";
}
