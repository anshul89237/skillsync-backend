package com.lpu.MentorService.mapper;

import com.lpu.MentorService.dto.MentorDTO;
import com.lpu.MentorService.entity.Mentor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MentorMapper {
    public MentorDTO toDto(Mentor mentor) {
        if (mentor == null) {
            return null;
        }

        MentorDTO dto = new MentorDTO();
        dto.setId(mentor.getId());
        dto.setUserId(mentor.getUserId());
        dto.setBio(mentor.getBio());
        dto.setExperience(mentor.getExperience());
        dto.setRating(mentor.getRating());
        dto.setHourlyRate(mentor.getHourlyRate());
        dto.setPhone(mentor.getPhone());
        dto.setDob(mentor.getDob());
        dto.setLocation(mentor.getLocation());
        dto.setStatus(mentor.getStatus());
        return dto;
    }

    public Mentor toEntity(MentorDTO mentorDTO) {
        if (mentorDTO == null) {
            return null;
        }

        Mentor mentor = new Mentor();
        mentor.setId(mentorDTO.getId());
        mentor.setUserId(mentorDTO.getUserId());
        mentor.setBio(mentorDTO.getBio());
        mentor.setExperience(mentorDTO.getExperience());
        mentor.setRating(mentorDTO.getRating());
        mentor.setHourlyRate(mentorDTO.getHourlyRate());
        mentor.setPhone(mentorDTO.getPhone());
        mentor.setDob(mentorDTO.getDob());
        mentor.setLocation(mentorDTO.getLocation());
        mentor.setStatus(mentorDTO.getStatus());
        return mentor;
    }

    public List<MentorDTO> toDtoList(List<Mentor> mentors) {
        if (mentors == null || mentors.isEmpty()) {
            return Collections.emptyList();
        }
        return mentors.stream().map(this::toDto).collect(Collectors.toList());
    }
}
