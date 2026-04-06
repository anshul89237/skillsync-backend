package com.lpu.GroupService.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsDTO {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
}
