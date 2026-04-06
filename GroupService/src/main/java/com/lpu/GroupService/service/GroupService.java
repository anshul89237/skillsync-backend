package com.lpu.GroupService.service;

import java.util.List;
import com.lpu.GroupService.dto.GroupsDTO;
import com.lpu.GroupService.dto.GroupMembersDTO;
import com.lpu.GroupService.dto.GroupRequestDTO;

public interface GroupService {

    // Group Management
    GroupsDTO createGroup(GroupRequestDTO dto);

    GroupsDTO updateGroupById(Long id, GroupsDTO groupDTO);

    void deleteGroupById(Long id);

    GroupsDTO findGroupById(Long id);

    List<GroupsDTO> findAllGroups();

    // Member Management
    String addMember(Long groupId, Long userId);

    String removeMember(Long groupId, Long userId);

    List<GroupMembersDTO> getMembers(Long groupId);
}
