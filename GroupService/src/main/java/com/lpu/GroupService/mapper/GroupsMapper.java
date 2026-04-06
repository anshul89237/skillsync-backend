package com.lpu.GroupService.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lpu.GroupService.dto.GroupsDTO;
import com.lpu.GroupService.entity.Groups;

@Component
public class GroupsMapper {

    public GroupsDTO toDto(Groups groups) {
        if (groups == null) {
            return null;
        }

        GroupsDTO dto = new GroupsDTO();
        dto.setId(groups.getId());
        dto.setUserId(groups.getUserId());
        dto.setName(groups.getName());
        dto.setDescription(groups.getDescription());
        return dto;
    }

    public Groups toEntity(GroupsDTO groupsDTO) {
        if (groupsDTO == null) {
            return null;
        }

        Groups entity = new Groups();
        entity.setId(groupsDTO.getId());
        entity.setUserId(groupsDTO.getUserId());
        entity.setName(groupsDTO.getName());
        entity.setDescription(groupsDTO.getDescription());
        return entity;
    }

    public List<GroupsDTO> toDtoList(List<Groups> groupsList) {
        if (groupsList == null || groupsList.isEmpty()) {
            return Collections.emptyList();
        }
        return groupsList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
