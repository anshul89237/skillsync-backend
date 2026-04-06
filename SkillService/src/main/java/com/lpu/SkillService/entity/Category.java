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
@Table(name = "categories", 
       uniqueConstraints = @UniqueConstraint(columnNames = "name"),
       indexes = @Index(name = "idx_category_name", columnList = "name"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String name;
 
    private String description;
}
