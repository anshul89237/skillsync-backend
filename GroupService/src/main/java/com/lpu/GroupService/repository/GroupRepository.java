package com.lpu.GroupService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lpu.GroupService.entity.Groups;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Long>{

	@Modifying
	@Query("DELETE FROM GroupMembers gm WHERE gm.groupId = :groupId AND gm.userId = :userId")
	void deleteByGroupIdAndUserId(Long groupId, Long userId);
}
