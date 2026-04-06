package com.lpu.MentorService.service;

import java.util.List;
import com.lpu.MentorService.dto.MentorDTO;
import com.lpu.MentorService.dto.MentorProfileDTO;
import com.lpu.MentorService.entity.MentorSkill;

public interface MentorService {

    // Profile Management
    MentorDTO saveMentor(MentorDTO mentorDTO);

    MentorDTO updateMentorById(Long id, MentorDTO mentorDTO);

    void deleteMentorById(Long id);

    MentorDTO updateRating(Long mentorId, Double rating);

    // Skill Management
    MentorSkill addSkill(Long userId, Long skillId);

    void removeSkill(Long userId, Long skillId);

    // Query Methods
    MentorDTO findMentorById(Long id);

    List<MentorDTO> findMentors();

    MentorDTO findMentorByUserId(Long userId);

    List<MentorDTO> getApplications();

    MentorDTO approveApplication(Long id);

    MentorDTO rejectApplication(Long id);

    MentorProfileDTO getMentorProfile(Long userId);
}
