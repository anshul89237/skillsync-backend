package com.lpu.GroupService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.GroupService.dto.GroupRequestDTO;
import com.lpu.GroupService.dto.GroupsDTO;
import com.lpu.GroupService.entity.GroupMembers;
import com.lpu.GroupService.entity.Groups;
import com.lpu.GroupService.exception.GroupNotFoundException;
import com.lpu.GroupService.mapper.GroupsMapper;
import com.lpu.GroupService.repository.GroupMembersRepository;
import com.lpu.GroupService.repository.GroupRepository;
import com.lpu.GroupService.service.GroupMemberCommandService;
import com.lpu.java.common_security.config.SecurityUtils;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupMembersRepository groupMembersRepository;
    @Mock
    private GroupMemberCommandService groupMemberCommandService;
    @Mock
    private GroupsMapper groupsMapper;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Counter counter;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Groups group;
    private GroupsDTO groupDTO;
    private MockedStatic<SecurityUtils> mockedSecurityUtils;

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = mockStatic(SecurityUtils.class);

        group = new Groups();
        group.setId(1L);
        group.setName("Java Study Group");
        group.setUserId(10L);

        groupDTO = new GroupsDTO();
        groupDTO.setId(1L);
        groupDTO.setName("Java Study Group");
        groupDTO.setUserId(10L);
        
        lenient().when(meterRegistry.counter(anyString())).thenReturn(counter);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
    }

    @Test
    void shouldCreateGroup_andAddCreatorAsMember() {
        // given
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(10L);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserName).thenReturn("MentorName");

        GroupRequestDTO dto = new GroupRequestDTO();
        dto.setName("Java Study Group");
        dto.setUserIds(Arrays.asList(11L, 12L));

        when(groupRepository.save(any(Groups.class))).thenReturn(group);
        when(groupsMapper.toDto(any(Groups.class))).thenReturn(groupDTO);

        // when
        GroupsDTO savedGroup = groupService.createGroup(dto);

        // then
        assertNotNull(savedGroup);
        verify(groupRepository).save(any(Groups.class));
        verify(groupMemberCommandService).addUserToGroup(eq(1L), eq(10L)); // Creator
        verify(groupMemberCommandService, times(3)).addUserToGroup(anyLong(), anyLong()); // Creator + 2 members
    }

    @Test
    void shouldDeleteGroup_andCleanupMembers() {
        // given
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupMembersRepository.findByGroupId(1L)).thenReturn(Arrays.asList(new GroupMembers()));

        // when
        groupService.deleteGroupById(1L);

        // then
        verify(groupMembersRepository).deleteAll(anyList());
        verify(groupRepository).deleteById(1L);
    }

    @Test
    void shouldThrowException_whenGroupNotFound() {
        // given
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(GroupNotFoundException.class, () -> groupService.findGroupById(1L));
    }
}
