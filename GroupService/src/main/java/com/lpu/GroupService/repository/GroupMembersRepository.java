package com.lpu.GroupService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.GroupService.entity.GroupMembers;

@Repository
public interface GroupMembersRepository extends JpaRepository<GroupMembers, Long>{

	List<GroupMembers> findByGroupId(Long groupId);

    Optional<GroupMembers> findByGroupIdAndUserId(Long groupId, Long userId);

    void deleteByGroupIdAndUserId(Long groupId, Long userId);
    
    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
}
