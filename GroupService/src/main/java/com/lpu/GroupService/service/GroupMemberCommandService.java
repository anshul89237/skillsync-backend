package com.lpu.GroupService.service;
 
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
 
import com.lpu.GroupService.client.UserServiceClient;
import com.lpu.GroupService.dto.UsersDTO;
import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.repository.GroupMembersRepository;
import com.lpu.GroupService.repository.GroupRepository;

import lombok.RequiredArgsConstructor;
 
@Service
@RequiredArgsConstructor
public class GroupMemberCommandService {
 
	private static final Logger logger = LoggerFactory.getLogger(GroupMemberCommandService.class);
 
	private final GroupMembersRepository repository;
	private final UserServiceClient userClient;
	private final GroupRepository groupsRepository;
 
	@CacheEvict(value = {"groupMembers", "groups"}, allEntries = true)
	public GroupMembers addUserToGroup(Long groupId, Long userId) {
		logger.info("Adding user to group. Group ID: {}, User ID: {}", groupId, userId);
 
		/// Validate Group
		groupsRepository.findById(groupId).orElseThrow(() -> {
			logger.error("Failed to add user to group: Group ID {} not found", groupId);
			return new RuntimeException("Group not found");
		});
 
		// Validate User
		logger.debug("Validating user ID: {} via UserServiceClient...", userId);
		UsersDTO user = userClient.findUserById(userId);
		if (user == null) {
			logger.error("Failed to add user to group: User ID {} not found via UserServiceClient", userId);
			throw new RuntimeException("User not found with id: " + userId);
		}
 
		// Prevent duplicate
		if (repository.existsByGroupIdAndUserId(groupId, userId)) {
			logger.warn("User ID {} is already a member of Group ID {}", userId, groupId);
			throw new RuntimeException("User already in group");
		}
 
		// Save
		GroupMembers gm = new GroupMembers();
		gm.setGroupId(groupId);
		gm.setUserId(userId);
		gm.setJoinedAt(LocalDateTime.now());
 
		GroupMembers savedMember = repository.save(gm);
		logger.info("Successfully added user ID {} to group ID {}. Membership ID: {}", userId, groupId, savedMember.getId());
		return savedMember;
	}
	
	@CacheEvict(value = {"groupMembers", "groups"}, allEntries = true)
	public GroupMembers deleteGroupMembersById(Long id) {
		logger.info("Removing group membership record ID: {}", id);
		GroupMembers gm = repository.findById(id)
	            .orElseThrow(() -> {
					logger.error("Membership removal failed: Membership record ID {} not found", id);
					return new RuntimeException("Group member not found");
				});
 
	    repository.deleteById(id);
		logger.info("Successfully removed membership record ID: {}. Group ID was: {}, User ID was: {}", id, gm.getGroupId(), gm.getUserId());
	    return gm;
	}

    @CacheEvict(value = {"groupMembers", "groups"}, allEntries = true)
	public void removeUserFromGroup(Long groupId, Long userId) {
		logger.info("Removing user from group. Group ID: {}, User ID: {}", groupId, userId);
		GroupMembers gm = repository.findByGroupIdAndUserId(groupId, userId)
	            .orElseThrow(() -> {
					logger.error("Member removal failed: User ID {} not found in Group ID {}", userId, groupId);
					return new RuntimeException("Group member not found");
				});
		repository.delete(gm);
		logger.info("Successfully removed user ID {} from group ID {}", userId, groupId);
	}
}
