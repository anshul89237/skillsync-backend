package com.lpu.GroupService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.GroupService.dto.GroupRequestDTO;
import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.entity.Groups;
import com.lpu.GroupService.service.GroupService;

@RequestMapping("/groups")
@RestController
public class GroupController {

	@Autowired
	private GroupService groupService;

	// Create group with members
	@PostMapping
	public ResponseEntity<Groups> createGroup(@RequestBody GroupRequestDTO dto) {
		return ResponseEntity.ok(groupService.createGroup(dto));
	}

	// Add member
	@PostMapping("/{groupId}/add/{userId}")
	public ResponseEntity<String> addMember(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {

		return ResponseEntity.ok(groupService.addMember(groupId, userId));
	}

	// Remove member
	@DeleteMapping("/{groupId}/remove/{userId}")
	public ResponseEntity<String> removeMember(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {

		return ResponseEntity.ok(groupService.removeMember(groupId, userId));
	}

	// Get all members
	@GetMapping("/{groupId}/members")
	public ResponseEntity<List<GroupMembers>> getMembers(@PathVariable("groupId") Long groupId) {
		return ResponseEntity.ok(groupService.getMembers(groupId));
	}

	@GetMapping("/{id}")
	public Groups findGroupById(@PathVariable("id") Long id) {
		return groupService.findGroupById(id);
	}

	@GetMapping
	public List<Groups> findAllGroups() {
		return groupService.findAllGroups();
	}

	@PutMapping("/{id}")
	public Groups updateGroupById(@PathVariable("id") Long id, @RequestBody Groups group) {
		return groupService.updateGroupById(id, group);
	}

	@DeleteMapping("/{id}")
	public void deleteGroupById(@PathVariable("id") Long id) {
		groupService.deleteGroupById(id);
	}
}
