package com.lpu.GroupService.entity;

import com.lpu.java.common_security.audit.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "groups", indexes = {
    @Index(name = "idx_groups_user_id", columnList = "userId"),
    @Index(name = "idx_groups_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Groups extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String name;
	private String description;
}
