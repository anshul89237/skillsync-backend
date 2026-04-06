package com.lpu.SkillService.service;

import java.util.List;
import com.lpu.SkillService.dto.CategoryDTO;
import com.lpu.SkillService.dto.SkillDTO;

public interface SkillService {

    // Skill Methods
    SkillDTO saveSkill(SkillDTO skillDTO);

    SkillDTO updateSkillById(Long id, SkillDTO skillDTO);

    void deleteSkillById(Long id);

    SkillDTO findSkillById(Long id);

    List<SkillDTO> findAllSkills();

    List<SkillDTO> findSkillsByCategoryId(Long categoryId);

    List<SkillDTO> findSkillsByIds(List<Long> ids);

    // Category Methods
    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    List<CategoryDTO> findAllCategories();

    CategoryDTO findCategoryById(Long id);
}
