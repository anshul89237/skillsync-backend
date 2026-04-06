package com.lpu.GroupService.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.GroupService.dto.GroupRequestDTO;
import com.lpu.GroupService.dto.GroupsDTO;
import com.lpu.GroupService.service.GroupService;
import com.lpu.java.common_security.config.JwtUtil;

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateGroup() throws Exception {
        GroupRequestDTO dto = new GroupRequestDTO();
        dto.setName("New Group");
        
        GroupsDTO result = new GroupsDTO();
        result.setId(1L);
        result.setName("New Group");
        when(groupService.createGroup(any(GroupRequestDTO.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("New Group"));
    }

    @Test
    void shouldAddMember() throws Exception {
        when(groupService.addMember(1L, 10L)).thenReturn("User added successfully");

        mockMvc.perform(post("/api/v1/groups/1/add/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("User added successfully"));
    }

    @Test
    void shouldRemoveMember() throws Exception {
        when(groupService.removeMember(1L, 10L)).thenReturn("User removed successfully");

        mockMvc.perform(delete("/api/v1/groups/1/remove/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("User removed successfully"));
    }

    @Test
    void shouldReturnGroupById() throws Exception {
        GroupsDTO result = new GroupsDTO();
        result.setId(1L);
        result.setName("Java Group");
        when(groupService.findGroupById(1L)).thenReturn(result);

        mockMvc.perform(get("/api/v1/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Java Group"));
    }
}
