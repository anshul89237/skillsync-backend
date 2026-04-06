package com.lpu.MentorService.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String bio;
    private Double experience;
    private Double rating;
    private Double hourlyRate;
    private String phone;
    private LocalDate dob;
    private String location;
    private String status;
    private List<SkillDTO> skills;
}
