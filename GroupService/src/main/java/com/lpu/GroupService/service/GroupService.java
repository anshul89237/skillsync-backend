package com.lpu.GroupService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.GroupService.client.UserServiceClient;
import com.lpu.GroupService.dto.GroupRequestDTO;
import com.lpu.GroupService.dto.UsersDTO;
import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.entity.Groups;
import com.lpu.GroupService.exception.GroupNotFoundException;
import com.lpu.GroupService.exception.UnauthorizedException;
import com.lpu.GroupService.repository.GroupMembersRepository;
import com.lpu.GroupService.repository.GroupRepository;

import jakarta.transaction.Transactional;

@Service
public class GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private GroupMembersRepository groupMembersRepository;

	@Autowired
	private GroupMemberService groupMemberService;

	@Autowired
	private UserServiceClient userClient;

	public Groups createGroup(GroupRequestDTO dto) 
	{
		UsersDTO user = userClient.findUserById(dto.getUserId());

		if (user == null) 
		{
			throw new GroupNotFoundException("User not found with id: " + dto.getUserId());
		}

		if (!user.getRole().equalsIgnoreCase("ROLE_MENTOR")) 
		{
			throw new UnauthorizedException("Only a mentor can create a group");
		}

		Groups group = new Groups();
		
		group.setName(dto.getName());
		group.setDescription(dto.getDescription());
		
		group.setUserId(user.getId());
		group.setCreated_by(user.getName());
		group.setCreated_at(LocalDateTime.now());

		Groups savedGroup = groupRepository.save(group);

		if (dto.getUserIds() != null) 
		{
			dto.getUserIds().forEach(userId -> groupMemberService.addUserToGroup(savedGroup.getId(), userId));
		}

		return savedGroup;
	}

	public String addMember(Long groupId, Long userId) 
	{
		groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + groupId));

		groupMembersRepository.findByGroupIdAndUserId(groupId, userId).ifPresent(existing -> 
			{
				throw new RuntimeException("User is already a member of this group");
			});

		GroupMembers gm = new GroupMembers();
		
		gm.setGroupId(groupId);
		gm.setUserId(userId);
		gm.setJoined_at(LocalDateTime.now());

		groupMembersRepository.save(gm);
		
		return "User added successfully";
	}

	@Transactional
	public String removeMember(Long groupId, Long userId) 
	{
		groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + groupId));
		
		groupMembersRepository.deleteByGroupIdAndUserId(groupId, userId);
		
		return "User removed successfully";
	}

	public List<GroupMembers> getMembers(Long groupId) 
	{
		groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + groupId));
	
		return groupMembersRepository.findByGroupId(groupId);
	}

	public Groups findGroupById(Long id) {
		return groupRepository.findById(id)
				.orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + id));
	}

	public List<Groups> findAllGroups() 
	{
		return groupRepository.findAll();
	}

	public Groups updateGroupById(Long id, Groups group) {
		Groups g = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + id));
		
		g.setName(group.getName());
		g.setDescription(group.getDescription());
		
		return groupRepository.save(g);
	}

	public void deleteGroupById(Long id) 
	{
		groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found with id: " + id));
		
		groupRepository.deleteById(id);
	}
}