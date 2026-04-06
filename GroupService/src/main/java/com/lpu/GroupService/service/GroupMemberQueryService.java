package com.lpu.GroupService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.repository.GroupMembersRepository;

@Service
public class GroupMemberQueryService {

	@Autowired
	private GroupMembersRepository repository;

	@Cacheable(value = "groupMember", key = "#id", unless = "#result == null")
	public GroupMembers findGroupMembersById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Cacheable(value = "groupMembersByGroup", key = "#groupId", unless = "#result == null || #result.isEmpty()")
	public List<GroupMembers> getMembersByGroupId(Long group_id) {
		return repository.findByGroupId(group_id);
	}

	@Cacheable(value = "allGroupMembers", unless = "#result == null || #result.isEmpty()")
	public List<GroupMembers> findAllGroupMembers() {
		return repository.findAll();
	}
}
