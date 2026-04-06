package com.lpu.LearnerService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearnerProfileDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long userId;

    private String name;     // from User Service
    private String email;    // from User Service

    private String phone;
    private LocalDate dob;
    private int age;         // calculated

    private String location;
    private String bio;

    private List<SkillDTO> skills;
}