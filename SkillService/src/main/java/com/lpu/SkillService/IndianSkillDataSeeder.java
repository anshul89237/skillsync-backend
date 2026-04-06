package com.lpu.SkillService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lpu.SkillService.entity.Skill;
import com.lpu.SkillService.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IndianSkillDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IndianSkillDataSeeder.class);

    private final SkillRepository skillRepository;

    @Override
    public void run(String... args) {
        List<String> skills = List.of(
                "Java Backend Development",
                "Spring Boot Microservices",
                "React Frontend Development",
                "Data Structures and Algorithms",
                "System Design",
                "SQL and Database Design",
                "Docker and Kubernetes",
                "REST API Development");

        for (String skillName : skills) {
            if (skillRepository.existsByNameIgnoreCase(skillName)) {
                continue;
            }

            Skill skill = new Skill();
            skill.setName(skillName);
            skill.setCategory("PROGRAMMING");
            skill.setCategoryId(null);
            skillRepository.save(skill);
            log.info("Seeded skill: {}", skillName);
        }
    }
}
