package com.lpu.GroupService.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.GroupService.dto.GroupMembersDTO;
import com.lpu.GroupService.dto.GroupRequestDTO;
import com.lpu.GroupService.dto.GroupsDTO;
import com.lpu.GroupService.service.GroupService;
import com.lpu.java.common_security.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/groups")
@RestController
@RequiredArgsConstructor
public class GroupController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

	// Create group with members
	@PostMapping
	public ResponseEntity<ApiResponse<GroupsDTO>> createGroup(@RequestBody GroupRequestDTO dto) {
		logger.info("Request to create group: {}", dto.getName());
		return ResponseEntity.ok(ApiResponse.success("Group created successfully", groupService.createGroup(dto)));
	}

	// Add member
	@PostMapping("/{groupId}/add/{userId}")
	public ResponseEntity<ApiResponse<String>> addMember(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {
		logger.info("Request to add User ID {} to Group ID {}", userId, groupId);
		return ResponseEntity.ok(ApiResponse.success("Member added successfully", groupService.addMember(groupId, userId)));
	}

	// Remove member
	@DeleteMapping("/{groupId}/remove/{userId}")
	public ResponseEntity<ApiResponse<String>> removeMember(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {
		logger.info("Request to remove User ID {} from Group ID {}", userId, groupId);
		return ResponseEntity.ok(ApiResponse.success("Member removed successfully", groupService.removeMember(groupId, userId)));
	}

	// Get all members
	@GetMapping("/{groupId}/members")
	public ResponseEntity<ApiResponse<List<GroupMembersDTO>>> getMembers(@PathVariable("groupId") Long groupId) {
		logger.info("Request to fetch all members for Group ID: {}", groupId);
		return ResponseEntity.ok(ApiResponse.success("Fetched all members", groupService.getMembers(groupId)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<GroupsDTO>> findGroupById(@PathVariable("id") Long id) {
		logger.info("Request to fetch group by ID: {}", id);
		return ResponseEntity.ok(ApiResponse.success("Fetched group by ID", groupService.findGroupById(id)));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<GroupsDTO>>> findAllGroups() {
		logger.info("Request to fetch all groups");
		return ResponseEntity.ok(ApiResponse.success("Fetched all groups", groupService.findAllGroups()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<GroupsDTO>> updateGroupById(@PathVariable("id") Long id, @RequestBody GroupsDTO groupDTO) {
		logger.info("Request to update group ID: {}", id);
		return ResponseEntity.ok(ApiResponse.success("Group updated successfully", groupService.updateGroupById(id, groupDTO)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteGroupById(@PathVariable("id") Long id) {
		logger.info("Request to delete group ID: {}", id);
		groupService.deleteGroupById(id);
		return ResponseEntity.ok(ApiResponse.success("Group deleted successfully", null));
	}
}
