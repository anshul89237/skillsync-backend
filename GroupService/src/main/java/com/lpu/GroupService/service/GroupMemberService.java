package com.lpu.GroupService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.GroupService.client.UserServiceClient;
import com.lpu.GroupService.dto.UsersDTO;
import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.repository.GroupMembersRepository;
import com.lpu.GroupService.repository.GroupRepository;

@Service
public class GroupMemberService {

	@Autowired
	private GroupMembersRepository repository;

	@Autowired
	private UserServiceClient userClient;

	@Autowired
	private GroupRepository groupsRepository;

	public GroupMembers addUserToGroup(Long groupId, Long userId) {

		/// Validate Group
		groupsRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

		// Validate User
		UsersDTO user = userClient.findUserById(userId);
		if (user == null) {
			throw new RuntimeException("User not found with id: " + userId);
		}

		// Prevent duplicate
		if (repository.existsByGroupIdAndUserId(groupId, userId)) {
			throw new RuntimeException("User already in group");
		}

		// Save
		GroupMembers gm = new GroupMembers();
		gm.setGroupId(groupId);
		gm.setUserId(userId);
		gm.setJoined_at(LocalDateTime.now());

		return repository.save(gm);
	}

	public GroupMembers findGroupMembersById(Long id) {
		return repository.findById(id).orElse(null);
	}

	public List<GroupMembers> getMembersByGroupId(Long group_id) {
		return repository.findByGroupId(group_id);
	}

	public List<GroupMembers> findAllGroupMembers() {
		return repository.findAll();
	}

	public void deleteGroupMembersById(Long id) {
		repository.deleteById(id);
	}
}
