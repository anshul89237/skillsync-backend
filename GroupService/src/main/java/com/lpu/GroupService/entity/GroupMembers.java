package com.lpu.GroupService.entity;

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
@Table(name = "group_members", indexes = {
    @Index(name = "idx_groupmembers_group_id", columnList = "groupId"),
    @Index(name = "idx_groupmembers_user_id", columnList = "userId")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupMembers extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Long groupId;
	private Long userId;
	private LocalDateTime joinedAt;
}
