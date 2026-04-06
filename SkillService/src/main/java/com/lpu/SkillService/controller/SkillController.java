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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.SkillService.dto.SkillDTO;
import com.lpu.SkillService.service.SkillService;
import com.lpu.java.common_security.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    private final SkillService skillService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillDTO>> findSkillById(@PathVariable("id") Long id) {
        logger.info("Request to fetch skill by ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Fetched skill by ID", skillService.findSkillById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillDTO>>> findAllSkills() {
        logger.info("Request to fetch all skills");
        return ResponseEntity.ok(ApiResponse.success("Fetched all skills", skillService.findAllSkills()));
    }

    @GetMapping("/batch")
    public ResponseEntity<ApiResponse<List<SkillDTO>>> findSkillsByIds(@RequestParam List<Long> ids) {
        logger.info("Request to fetch skills by batch IDs: {}", ids);
        return ResponseEntity.ok(ApiResponse.success("Fetched batch skills", skillService.findSkillsByIds(ids)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<SkillDTO>>> findSkillsByCategoryId(@PathVariable Long categoryId) {
        logger.info("Request to fetch skills for category identifier: {}", categoryId);
        return ResponseEntity.ok(ApiResponse.success("Fetched skills by category", skillService.findSkillsByCategoryId(categoryId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SkillDTO>> saveSkill(@RequestBody SkillDTO skillDTO) {
        logger.info("Admin request to create new skill: {}", skillDTO.getName());
        return ResponseEntity.ok(ApiResponse.success("Skill created successfully", skillService.saveSkill(skillDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillDTO>> updateSkillById(@PathVariable("id") Long id, @RequestBody SkillDTO skillDTO) {
        logger.info("Admin request to update skill ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Skill updated successfully", skillService.updateSkillById(id, skillDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSkillById(@PathVariable("id") Long id) {
        logger.info("Admin request to delete skill ID: {}", id);
        skillService.deleteSkillById(id);
        return ResponseEntity.ok(ApiResponse.success("Skill deleted successfully", null));
    }
}
