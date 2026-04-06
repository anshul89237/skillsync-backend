package com.lpu.SkillService.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.SkillService.dto.CategoryDTO;
import com.lpu.SkillService.service.SkillService;
import com.lpu.java.common_security.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final SkillService skillService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> findCategoryById(@PathVariable("id") Long id) {
        logger.info("Request to fetch category by ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Fetched category by ID", skillService.findCategoryById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> findAllCategories() {
        logger.info("Request to fetch all categories");
        return ResponseEntity.ok(ApiResponse.success("Fetched all categories", skillService.findAllCategories()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        logger.info("Admin request to create new category: {}", categoryDTO.getName());
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", skillService.saveCategory(categoryDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
        logger.info("Admin request to update category ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", skillService.updateCategory(id, categoryDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable("id") Long id) {
        logger.info("Admin request to delete category ID: {}", id);
        skillService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
}
