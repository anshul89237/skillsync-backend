package com.lpu.LearnerService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.LearnerService.client.SkillServiceClient;
import com.lpu.LearnerService.client.UserServiceClient;
import com.lpu.LearnerService.dto.LearnerProfileDTO;
import com.lpu.LearnerService.dto.SkillDTO;
import com.lpu.LearnerService.dto.UsersDTO;
import com.lpu.LearnerService.entity.LearnerProfile;
import com.lpu.LearnerService.entity.LearnerSkill;
import com.lpu.LearnerService.mapper.LearnerMapper;
import com.lpu.LearnerService.repository.LearnerRepository;
import com.lpu.LearnerService.repository.LearnerSkillRepository;

@ExtendWith(MockitoExtension.class)
class LearnerServiceImplTest {

    @Mock
    private LearnerRepository repo;
    
    @Mock
    private LearnerSkillRepository learnerSkillRepository;
    
    @Mock
    private UserServiceClient userClient;
    
    @Mock
    private SkillServiceClient skillClient;

    @Mock
    private LearnerMapper learnerMapper;

    @InjectMocks
    private LearnerServiceImpl learnerService;

    private LearnerProfile profile;
    private LearnerProfileDTO profileDTO;
    private UsersDTO usersDTO;

    @BeforeEach
    void setUp() {
        profile = new LearnerProfile();
        profile.setId(1L);
        profile.setUserId(1L);
        profile.setDob(LocalDate.of(2000, 1, 1));
        profile.setLocation("New York");
        profile.setPhone("1234567890");

        profileDTO = new LearnerProfileDTO();
        profileDTO.setUserId(1L);
        profileDTO.setName("John Doe");

        usersDTO = new UsersDTO();
        usersDTO.setId(1L);
        usersDTO.setName("John Doe");
        usersDTO.setEmail("john@example.com");
    }

    @Test
    void shouldCreateProfile_whenValidProfile() {
        // given
        when(learnerMapper.toEntity(any(LearnerProfileDTO.class))).thenReturn(profile);
        when(repo.save(any(LearnerProfile.class))).thenReturn(profile);
        when(learnerMapper.toDto(any(LearnerProfile.class))).thenReturn(profileDTO);

        // when
        LearnerProfileDTO savedDTO = learnerService.createProfile(profileDTO);

        // then
        assertNotNull(savedDTO);
        verify(userClient).updateUserRole(anyLong(), eq("ROLE_LEARNER"));
        verify(repo).save(profile);
    }

    @Test
    void shouldAddSkill_whenValidInput() {
        // given
        LearnerSkill learnerSkill = new LearnerSkill();
        learnerSkill.setId(1L);
        
        when(learnerSkillRepository.save(any(LearnerSkill.class))).thenReturn(learnerSkill);

        // when
        learnerService.addSkill(1L, 10L);

        // then
        verify(learnerSkillRepository).save(any(LearnerSkill.class));
    }

    @Test
    void shouldRemoveSkill() {
        // when
        learnerService.removeSkill(1L, 10L);

        // then
        verify(learnerSkillRepository).deleteByUserIdAndSkillId(1L, 10L);
    }

    @Test
    void shouldReturnProfileDTO_whenUserIdExists() {
        // given
        when(repo.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(userClient.findUserById(1L)).thenReturn(usersDTO);
        
        LearnerSkill skillMapping = new LearnerSkill();
        skillMapping.setSkillId(10L);
        when(learnerSkillRepository.findByUserId(1L)).thenReturn(Arrays.asList(skillMapping));
        
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(10L);
        skillDTO.setName("Java");
        when(skillClient.findSkillsByIds(anyList())).thenReturn(Arrays.asList(skillDTO));

        // when
        LearnerProfileDTO result = learnerService.getProfile(1L);

        // then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals(1, result.getSkills().size());
        verify(repo).findByUserId(1L);
    }

    @Test
    void shouldThrowException_whenProfileNotFound() {
        // given
        when(repo.findByUserId(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> learnerService.getProfile(1L));
    }
}
