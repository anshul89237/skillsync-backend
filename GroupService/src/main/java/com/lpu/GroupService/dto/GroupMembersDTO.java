package com.lpu.GroupService.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersDTO {
    private Long id;
    private Long groupId;
    private Long userId;
    private LocalDateTime joined_at;
}
