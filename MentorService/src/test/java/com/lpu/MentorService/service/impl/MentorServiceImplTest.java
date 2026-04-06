package com.lpu.MentorService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.MentorService.client.SkillServiceClient;
import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.MentorDTO;
import com.lpu.MentorService.dto.MentorProfileDTO;
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.exception.MentorNotFoundException;
import com.lpu.MentorService.mapper.MentorMapper;
import com.lpu.MentorService.repository.MentorRepository;
import com.lpu.MentorService.repository.MentorSkillRepository;
import com.lpu.java.common_security.dto.ApiResponse;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
class MentorServiceImplTest {

    @Mock
    private MentorRepository repository;
    
    @Mock
    private MentorSkillRepository mentorSkillRepository;
    
    @Mock
    private UserServiceClient userClient;
    
    @Mock
    private SkillServiceClient skillClient;

    @Mock
    private MentorMapper mentorMapper;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @InjectMocks
    private MentorServiceImpl mentorService;

    private Mentor mentor;
    private MentorDTO mentorDTO;
    private UsersDTO usersDTO;

    @BeforeEach
    void setUp() {
        mentor = new Mentor();
        mentor.setId(1L);
        mentor.setUserId(1L);
        mentor.setStatus("PENDING");
        mentor.setDob(LocalDate.of(1990, 1, 1));
        mentor.setPhone("1234567890");
        mentor.setExperience(5.0);
        mentor.setHourlyRate(1200.0);
        mentor.setRating(4.5);
        mentor.setLocation("Chandigarh");
        mentor.setBio("Backend mentor");

        mentorDTO = new MentorDTO();
        mentorDTO.setId(1L);
        mentorDTO.setUserId(1L);
        mentorDTO.setStatus("PENDING");

        usersDTO = new UsersDTO();
        usersDTO.setId(1L);
        usersDTO.setName("Jane Doe");
        usersDTO.setEmail("jane@example.com");

        lenient().when(meterRegistry.counter(anyString())).thenReturn(counter);
    }

    @Test
    void shouldSaveMentor_whenValidInput() {
        // given
        when(mentorMapper.toEntity(any(MentorDTO.class))).thenReturn(mentor);
        when(repository.save(any(Mentor.class))).thenReturn(mentor);
        when(mentorMapper.toDto(any(Mentor.class))).thenReturn(mentorDTO);

        // when
        MentorDTO saved = mentorService.saveMentor(mentorDTO);

        // then
        assertNotNull(saved);
        assertEquals("PENDING", saved.getStatus());
        verify(repository).save(mentor);
    }

    @Test
    void shouldApproveApplication_whenIdExists() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(mentor));
        when(repository.save(any(Mentor.class))).thenReturn(mentor);
        when(mentorMapper.toDto(any(Mentor.class))).thenReturn(mentorDTO);

        // when
        mentorService.approveApplication(1L);

        // then
        assertEquals("APPROVED", mentor.getStatus());
        verify(userClient).updateUserRole(anyLong(), eq("ROLE_MENTOR"));
    }

    @Test
    void shouldRejectApplication_whenIdExists() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(mentor));
        when(repository.save(any(Mentor.class))).thenReturn(mentor);
        when(mentorMapper.toDto(any(Mentor.class))).thenReturn(mentorDTO);

        // when
        mentorService.rejectApplication(1L);

        // then
        assertEquals("REJECTED", mentor.getStatus());
    }

    @Test
    void shouldReturnProfileDTO_whenUserIdExists() {
        // given
        when(repository.findByUserId(1L)).thenReturn(Optional.of(mentor));
        when(userClient.findUserById(1L)).thenReturn(ApiResponse.success("ok", usersDTO));
        
        MentorSkill skillMapping = new MentorSkill();
        skillMapping.setSkillId(10L);
        when(mentorSkillRepository.findByUserId(1L)).thenReturn(Arrays.asList(skillMapping));
        
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(10L);
        skillDTO.setName("Spring Boot");
        when(skillClient.findSkillsByIds(anyList())).thenReturn(ApiResponse.success("ok", Arrays.asList(skillDTO)));

        // when
        MentorProfileDTO result = mentorService.getMentorProfile(1L);

        // then
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals(1, result.getSkills().size());
    }

    @Test
    void shouldThrowException_whenMentorNotFound() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(MentorNotFoundException.class, () -> mentorService.findMentorById(1L));
    }
}
