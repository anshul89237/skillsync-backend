package com.lpu.ReviewService.entity;

import com.lpu.java.common_security.audit.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "reviews", indexes = {
    @Index(name = "idx_review_mentor_id", columnList = "mentorId"),
    @Index(name = "idx_review_user_id", columnList = "userId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long mentorId;
	private Long userId;
	private Double rating;
	private String comment;
}
