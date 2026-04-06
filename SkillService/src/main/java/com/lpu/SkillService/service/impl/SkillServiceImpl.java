package com.lpu.SkillService.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.lpu.SkillService.dto.CategoryDTO;
import com.lpu.SkillService.dto.SkillDTO;
import com.lpu.SkillService.entity.Category;
import com.lpu.SkillService.entity.Skill;
import com.lpu.SkillService.mapper.CategoryMapper;
import com.lpu.SkillService.mapper.SkillMapper;
import com.lpu.SkillService.repository.CategoryRepository;
import com.lpu.SkillService.repository.SkillRepository;
import com.lpu.SkillService.service.SkillService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

	private static final Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final SkillMapper skillMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"skills", "skillsByIds"}, allEntries = true)
    public SkillDTO saveSkill(SkillDTO skillDTO) {
		logger.info("Admin creating new skill: {}", skillDTO.getName());
        Skill skill = skillMapper.toEntity(skillDTO);
        Skill saved = skillRepository.save(skill);
		logger.info("Skill '{}' created successfully with ID: {}", saved.getName(), saved.getId());
		return skillMapper.toDto(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"skills", "skillsByIds"}, allEntries = true)
    public SkillDTO updateSkillById(Long id, SkillDTO skillDTO) {
		logger.info("Admin updating skill ID: {}", id);
        Skill skill = skillRepository.findById(id).orElseThrow(() -> {
			logger.error("Update failed: Skill ID {} not found", id);
			return new RuntimeException("Skill not found");
		});
        skill.setName(skillDTO.getName());
        skill.setCategoryId(skillDTO.getCategoryId());
        Skill updated = skillRepository.save(skill);
		logger.info("Skill ID {} updated successfully", id);
		return skillMapper.toDto(updated);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = {"skills", "skillsByIds"}, allEntries = true)
    public void deleteSkillById(Long id) {
		logger.info("Admin deleting skill ID: {}", id);
        skillRepository.deleteById(id);
		logger.info("Skill ID {} deleted successfully", id);
    }

    @Override
    @Cacheable(value = "skills", key = "#id")
    public SkillDTO findSkillById(Long id) {
		logger.info("Fetching skill details for ID: {}", id);
        Skill skill = skillRepository.findById(id).orElseThrow(() -> {
			logger.error("Skill lookup failed: ID {} not found", id);
			return new RuntimeException("Skill not found");
		});
		return skillMapper.toDto(skill);
    }

    @Override
	@Cacheable(value = "skills")
    public List<SkillDTO> findAllSkills() {
		logger.info("Fetching all available skills");
        List<Skill> skills = skillRepository.findAll();
		return skillMapper.toDtoList(skills);
    }

    @Override
    public List<SkillDTO> findSkillsByCategoryId(Long categoryId) {
		logger.info("Fetching skills for Category ID: {}", categoryId);
        List<Skill> skills = skillRepository.findByCategoryId(categoryId);
		return skillMapper.toDtoList(skills);
    }

    @Override
    @Cacheable(value = "skillsByIds", key = "#ids")
    public List<SkillDTO> findSkillsByIds(List<Long> ids) {
		logger.info("Batch fetching skills for IDs: {}", ids);
        List<Skill> skills = skillRepository.findAllById(ids);
		return skillMapper.toDtoList(skills);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
		logger.info("Admin creating new category: {}", categoryDTO.getName());
        Category category = categoryMapper.toEntity(categoryDTO);
        Category saved = categoryRepository.save(category);
		logger.info("Category '{}' created successfully", saved.getName());
		return categoryMapper.toDto(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
		logger.info("Admin updating category ID: {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
			logger.error("Update failed: Category ID {} not found", id);
			return new RuntimeException("Category not found");
		});
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        Category updated = categoryRepository.save(category);
		logger.info("Category ID {} updated successfully", id);
		return categoryMapper.toDto(updated);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long id) {
		logger.info("Admin deleting category ID: {}", id);
        categoryRepository.deleteById(id);
		logger.info("Category ID {} deleted successfully", id);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
		logger.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
		return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
		logger.info("Fetching category details for ID: {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
			logger.error("Category lookup failed: ID {} not found", id);
			return new RuntimeException("Category not found");
		});
		return categoryMapper.toDto(category);
    }
}
