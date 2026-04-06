package com.lpu.GroupService.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lpu.GroupService.dto.GroupMembersDTO;
import com.lpu.GroupService.dto.GroupRequestDTO;
import com.lpu.GroupService.dto.GroupsDTO;
import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.entity.Groups;
import com.lpu.GroupService.exception.GroupNotFoundException;
import com.lpu.GroupService.mapper.GroupMembersMapper;
import com.lpu.GroupService.mapper.GroupsMapper;
import com.lpu.GroupService.repository.GroupMembersRepository;
import com.lpu.GroupService.repository.GroupRepository;
import com.lpu.GroupService.service.GroupMemberCommandService;
import com.lpu.GroupService.service.GroupService;
import com.lpu.java.common_security.config.SecurityUtils;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final GroupMembersRepository groupMembersRepository;
    private final GroupMemberCommandService groupMemberCommandService;
    private final MeterRegistry meterRegistry;
    private final GroupsMapper groupsMapper;
    private final GroupMembersMapper groupMembersMapper;

    @Override
    @CachePut(value = "groups", key = "#result.id")
    public GroupsDTO createGroup(GroupRequestDTO dto) {
		logger.info("Group creation request received: {}", dto.getName());
		Groups group = new Groups();
		group.setName(dto.getName());
		group.setDescription(dto.getDescription());
		group.setUserId(SecurityUtils.getCurrentUserId());
		group.setCreatedBy(SecurityUtils.getCurrentUserName());
		group.setCreatedAt(LocalDateTime.now());

		Groups savedGroup = groupRepository.save(group);
		logger.info("Group saved with ID: {}", savedGroup.getId());

		// add current user (creator) as member
		groupMemberCommandService.addUserToGroup(savedGroup.getId(), savedGroup.getUserId());

		// add other members
		if (dto.getUserIds() != null) {
			for (Long uid : dto.getUserIds()) {
				groupMemberCommandService.addUserToGroup(savedGroup.getId(), uid);
			}
		}
        
        meterRegistry.counter("groups.created.total").increment();
		return groupsMapper.toDto(savedGroup);
    }

    @Override
    @CachePut(value = "groups", key = "#id")
    public GroupsDTO updateGroupById(Long id, GroupsDTO groupDTO) {
		logger.info("Updating group ID: {}", id);
		Groups g = groupRepository.findById(id).orElseThrow(() -> {
			logger.error("Update failed: Group ID {} not found", id);
			return new GroupNotFoundException("Group not found with id: " + id);
		});
		g.setName(groupDTO.getName());
		g.setDescription(groupDTO.getDescription());
		Groups updatedGroup = groupRepository.save(g);
		logger.info("Successfully updated group ID: {}", id);
		return groupsMapper.toDto(updatedGroup);
    }

    @Override
    @CacheEvict(value = "groups", key = "#id")
    public void deleteGroupById(Long id) {
		logger.info("Deleting group ID: {}", id);
		groupRepository.findById(id).orElseThrow(() -> {
			logger.error("Deletion failed: Group ID {} not found", id);
			return new GroupNotFoundException("Group not found with id: " + id);
		});
		// Delete all members
		List<GroupMembers> members = groupMembersRepository.findByGroupId(id);
		groupMembersRepository.deleteAll(members);
		groupRepository.deleteById(id);
		logger.info("Successfully deleted group ID: {} and its members", id);
    }

    @Override
    @Cacheable(value = "groups", key = "#id", unless = "#result == null")
    public GroupsDTO findGroupById(Long id) {
		logger.info("Fetching group details by ID: {}", id);
		Groups g = groupRepository.findById(id).orElseThrow(() -> {
			logger.error("Group lookup failed: ID {} not found", id);
			return new GroupNotFoundException("Group not found with id: " + id);
		});
		return groupsMapper.toDto(g);
    }

    @Override
    @Cacheable(value = "allGroups", unless = "#result == null || #result.isEmpty()")
    public List<GroupsDTO> findAllGroups() {
		logger.info("Admin request: Fetching all groups in the system");
		List<Groups> groups = groupRepository.findAll();
		return groupsMapper.toDtoList(groups);
    }

    @Override
    public List<GroupMembersDTO> getMembers(Long groupId) {
		logger.info("Fetching members for group ID: {}", groupId);
		List<GroupMembers> members = groupMembersRepository.findByGroupId(groupId);
		return groupMembersMapper.toDtoList(members);
    }

    @Override
    public String addMember(Long groupId, Long userId) {
		logger.info("Adding User ID {} to group ID: {}", userId, groupId);
		groupMemberCommandService.addUserToGroup(groupId, userId);
		return "Member added successfully";
    }

    @Override
    public String removeMember(Long groupId, Long userId) {
		logger.info("Removing User ID {} from group ID: {}", userId, groupId);
		groupMemberCommandService.removeUserFromGroup(groupId, userId);
		return "Member removed successfully";
    }
}
