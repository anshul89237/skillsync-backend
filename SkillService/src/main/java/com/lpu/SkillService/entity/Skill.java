package com.lpu.SkillService.entity;

import java.io.Serializable;

import com.lpu.java.common_security.audit.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skills", 
       uniqueConstraints = @UniqueConstraint(columnNames = "name"),
       indexes = {
           @Index(name = "idx_skill_name", columnList = "name"),
           @Index(name = "idx_skill_category_id", columnList = "categoryId")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Skill extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(nullable = false, unique = true)
	private String name;

    @Column(nullable = false, length = 100)
	private String category = "GENERAL";

	private Long categoryId;
}