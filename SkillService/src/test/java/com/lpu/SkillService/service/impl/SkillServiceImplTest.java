package com.lpu.SkillService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.SkillService.dto.CategoryDTO;
import com.lpu.SkillService.dto.SkillDTO;
import com.lpu.SkillService.entity.Category;
import com.lpu.SkillService.entity.Skill;
import com.lpu.SkillService.exception.SkillNotFoundException;
import com.lpu.SkillService.mapper.CategoryMapper;
import com.lpu.SkillService.mapper.SkillMapper;
import com.lpu.SkillService.repository.CategoryRepository;
import com.lpu.SkillService.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private SkillServiceImpl skillService;

    private Skill skill;
    private SkillDTO skillDTO;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        skill = new Skill();
        skill.setId(1L);
        skill.setName("Java");
        skill.setCategoryId(1L);

        skillDTO = new SkillDTO();
        skillDTO.setId(1L);
        skillDTO.setName("Java");
        skillDTO.setCategoryId(1L);

        category = new Category();
        category.setId(1L);
        category.setName("Programming");

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Programming");
    }

    @Test
    void shouldSaveSkill_whenValidInput() {
        // given
        when(skillMapper.toEntity(any(SkillDTO.class))).thenReturn(skill);
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(skillMapper.toDto(any(Skill.class))).thenReturn(skillDTO);

        // when
        SkillDTO savedSkill = skillService.saveSkill(skillDTO);

        // then
        assertNotNull(savedSkill);
        assertEquals("Java", savedSkill.getName());
        verify(skillRepository).save(any(Skill.class));
    }

    @Test
    void shouldUpdateSkill_whenSkillExists() {
        // given
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(skillMapper.toDto(any(Skill.class))).thenReturn(skillDTO);

        // when
        SkillDTO updatedSkill = skillService.updateSkillById(1L, skillDTO);

        // then
        assertNotNull(updatedSkill);
        verify(skillRepository).save(any(Skill.class));
    }

    @Test
    void shouldThrowException_whenUpdatingNonExistentSkill() {
        // given
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(SkillNotFoundException.class, () -> skillService.updateSkillById(1L, skillDTO));
    }

    @Test
    void shouldDeleteSkill_whenSkillExists() {
        // given
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        // when
        skillService.deleteSkillById(1L);

        // then
        verify(skillRepository).deleteById(1L);
    }

    @Test
    void shouldReturnSkill_whenSkillIdExists() {
        // given
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(skillMapper.toDto(any(Skill.class))).thenReturn(skillDTO);

        // when
        SkillDTO foundSkill = skillService.findSkillById(1L);

        // then
        assertNotNull(foundSkill);
        assertEquals(1L, foundSkill.getId());
    }

    @Test
    void shouldReturnAllSkills() {
        // given
        when(skillRepository.findAll()).thenReturn(Arrays.asList(skill));
        when(skillMapper.toDtoList(anyList())).thenReturn(Arrays.asList(skillDTO));

        // when
        List<SkillDTO> skills = skillService.findAllSkills();

        // then
        assertFalse(skills.isEmpty());
        assertEquals(1, skills.size());
    }

    @Test
    void shouldSaveCategory() {
        // given
        when(categoryMapper.toEntity(any(CategoryDTO.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDTO);

        // when
        CategoryDTO savedCategory = skillService.saveCategory(categoryDTO);

        // then
        assertNotNull(savedCategory);
        assertEquals("Programming", savedCategory.getName());
    }

    @Test
    void shouldReturnCategoryById() {
        // given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDTO);

        // when
        CategoryDTO foundCategory = skillService.findCategoryById(1L);

        // then
        assertNotNull(foundCategory);
        assertEquals(1L, foundCategory.getId());
    }
}
