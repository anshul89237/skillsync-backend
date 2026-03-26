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
import com.lpu.GroupService.service.GroupMemberService;

@RequestMapping("/groupmem")
@RestController
public class GroupMembersController {

	@Autowired
	private GroupMemberService service;

	@PostMapping
	public GroupMembers addUserToGroup(@RequestParam Long groupId, @RequestParam Long userId) {

		return service.addUserToGroup(groupId, userId);
	}

	@GetMapping("/{id}")
	public GroupMembers findGroupMembersById(@PathVariable("id") Long id) {
		return service.findGroupMembersById(id);
	}

	@GetMapping("/group/{group_id}")
	public List<GroupMembers> getMembersByGroupId(@PathVariable("group_id") Long group_id) {
		return service.getMembersByGroupId(group_id);
	}
	
	@GetMapping
	public List<GroupMembers> findAllGroupMembers() {
		return service.findAllGroupMembers();
	}
	
	@DeleteMapping("/{id}")
	public void deleteGroupMembersById(@PathVariable("id") Long id) {
		service.deleteGroupMembersById(id);
	}
}
