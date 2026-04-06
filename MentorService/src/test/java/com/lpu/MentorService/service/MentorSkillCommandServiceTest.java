package com.lpu.MentorService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.MentorService.dto.MentorSkillDTO;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorSkillRepository;

@ExtendWith(MockitoExtension.class)
class MentorSkillCommandServiceTest {

    @Mock
    private MentorSkillRepository repository;

    @InjectMocks
    private MentorSkillCommandService commandService;

    @Test
    void shouldAddSkillToMentor() {
        // given
        MentorSkillDTO dto = new MentorSkillDTO();
        dto.setMentorId(1L);
        dto.setSkillId(10L);
        
        MentorSkill result = new MentorSkill();
        result.setId(100L);
        when(repository.save(any(MentorSkill.class))).thenReturn(result);

        // when
        MentorSkill savedMapping = commandService.addSkillToMentor(dto);

        // then
        assertNotNull(savedMapping);
        verify(repository).save(any(MentorSkill.class));
    }

    @Test
    void shouldDeleteMentorSkill() {
        // when
        commandService.deleteMentorSkillById(1L);

        // then
        verify(repository).deleteById(1L);
    }
}
