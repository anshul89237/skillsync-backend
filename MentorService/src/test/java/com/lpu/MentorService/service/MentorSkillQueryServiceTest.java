package com.lpu.MentorService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorSkillRepository;

@ExtendWith(MockitoExtension.class)
class MentorSkillQueryServiceTest {

    @Mock
    private MentorSkillRepository repository;

    @InjectMocks
    private MentorSkillQueryService queryService;

    @Test
    void shouldReturnMentorSkillById_whenIdExists() {
        // given
        MentorSkill ms = new MentorSkill();
        ms.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(ms));

        // when
        MentorSkill result = queryService.findMentorSkillById(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldReturnAllMentorSkills() {
        // given
        when(repository.findAll()).thenReturn(Arrays.asList(new MentorSkill()));

        // when
        List<MentorSkill> result = queryService.findMentorSkills();

        // then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
