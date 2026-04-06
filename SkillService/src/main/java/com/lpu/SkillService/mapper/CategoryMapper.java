package com.lpu.SkillService.mapper;

import com.lpu.SkillService.dto.CategoryDTO;
import com.lpu.SkillService.entity.Category;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public CategoryDTO toDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    public List<CategoryDTO> toDtoList(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream().map(this::toDto).collect(Collectors.toList());
    }
}
