package com.lpu.MentorService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfileDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long mentorId;
    private Long userId;

    private String name;     // from User Service
    private String email;    // from User Service

    private String phone;
    private LocalDate dob;
    private int age;

    private String location;

    private String bio;
    private double experience;
    private double hourlyRate;
    private double rating;

    private List<SkillDTO> skills;
}
