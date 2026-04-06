package com.lpu.GroupService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.service.GroupMemberCommandService;
import com.lpu.GroupService.service.GroupMemberQueryService;

@RequestMapping("/groupmem")
@RestController
public class GroupMembersController {

	@Autowired
	private GroupMemberCommandService commandService;
	
	@Autowired
	private GroupMemberQueryService queryService;

	@PostMapping
	public GroupMembers addUserToGroup(@RequestParam Long groupId, @RequestParam Long userId) {

		return commandService.addUserToGroup(groupId, userId);
	}

	@GetMapping("/{id}")
	public GroupMembers findGroupMembersById(@PathVariable("id") Long id) {
		return queryService.findGroupMembersById(id);
	}

	@GetMapping("/group/{group_id}")
	public List<GroupMembers> getMembersByGroupId(@PathVariable("group_id") Long group_id) {
		return queryService.getMembersByGroupId(group_id);
	}
	
	@GetMapping
	public List<GroupMembers> findAllGroupMembers() {
		return queryService.findAllGroupMembers();
	}
	
	@DeleteMapping("/{id}")
	public void deleteGroupMembersById(@PathVariable("id") Long id) {
		commandService.deleteGroupMembersById(id);
	}
}
