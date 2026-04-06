package com.lpu.GroupService.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lpu.GroupService.dto.GroupMembersDTO;
import com.lpu.GroupService.entity.GroupMembers;

@Component
public class GroupMembersMapper {

    public GroupMembersDTO toDto(GroupMembers groupMembers) {
        if (groupMembers == null) {
            return null;
        }

        GroupMembersDTO dto = new GroupMembersDTO();
        dto.setId(groupMembers.getId());
        dto.setGroupId(groupMembers.getGroupId());
        dto.setUserId(groupMembers.getUserId());
        dto.setJoined_at(groupMembers.getJoinedAt());
        return dto;
    }

    public GroupMembers toEntity(GroupMembersDTO groupMembersDTO) {
        if (groupMembersDTO == null) {
            return null;
        }

        GroupMembers entity = new GroupMembers();
        entity.setId(groupMembersDTO.getId());
        entity.setGroupId(groupMembersDTO.getGroupId());
        entity.setUserId(groupMembersDTO.getUserId());
        entity.setJoinedAt(groupMembersDTO.getJoined_at());
        return entity;
    }

    public List<GroupMembersDTO> toDtoList(List<GroupMembers> groupMembersList) {
        if (groupMembersList == null || groupMembersList.isEmpty()) {
            return Collections.emptyList();
        }
        return groupMembersList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
