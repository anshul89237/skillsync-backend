package com.lpu.SkillService.mapper;

import com.lpu.SkillService.dto.SkillDTO;
import com.lpu.SkillService.entity.Skill;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkillMapper {
    public SkillDTO toDto(Skill skill) {
        if (skill == null) {
            return null;
        }

        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setCategoryId(skill.getCategoryId());
        return dto;
    }

    public Skill toEntity(SkillDTO skillDTO) {
        if (skillDTO == null) {
            return null;
        }

        Skill skill = new Skill();
        skill.setId(skillDTO.getId());
        skill.setName(skillDTO.getName());
        skill.setCategoryId(skillDTO.getCategoryId());
        return skill;
    }

    public List<SkillDTO> toDtoList(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) {
            return Collections.emptyList();
        }
        return skills.stream().map(this::toDto).collect(Collectors.toList());
    }
}
