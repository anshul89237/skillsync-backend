package com.lpu.MentorService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lpu.MentorService.client.SkillServiceClient;
import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.entity.MentorSkill;
import com.lpu.MentorService.repository.MentorRepository;
import com.lpu.MentorService.repository.MentorSkillRepository;
import com.lpu.java.common_security.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IndianMentorProfileSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IndianMentorProfileSeeder.class);

    private final MentorRepository mentorRepository;
    private final MentorSkillRepository mentorSkillRepository;
    private final UserServiceClient userServiceClient;
    private final SkillServiceClient skillServiceClient;

    @Override
    public void run(String... args) {
        try {
            Map<String, Long> skillIds = fetchSkillIdsByName();

            seedMentor(
                    "arjun.sharma@skillsync.com",
                    "Senior Java and Spring Boot mentor focused on interview prep and production microservices.",
                    8.0,
                    4.8,
                    999.0,
                    "9876543210",
                    LocalDate.of(1991, 6, 15),
                    "Bengaluru",
                    List.of("Java Backend Development", "Spring Boot Microservices", "REST API Development"),
                    skillIds);

            seedMentor(
                    "priya.nair@skillsync.com",
                    "Frontend and full-stack mentor with focus on React architecture and clean UI systems.",
                    7.0,
                    4.7,
                    899.0,
                    "9123456780",
                    LocalDate.of(1993, 2, 10),
                    "Kochi",
                    List.of("React Frontend Development", "System Design", "REST API Development"),
                    skillIds);

            seedMentor(
                    "rahul.verma@skillsync.com",
                    "Backend systems mentor specializing in SQL tuning, API design, and service reliability.",
                    9.0,
                    4.9,
                    1099.0,
                    "9988776655",
                    LocalDate.of(1989, 11, 20),
                    "Pune",
                    List.of("SQL and Database Design", "System Design", "Docker and Kubernetes"),
                    skillIds);

            seedMentor(
                    "neha.iyer@skillsync.com",
                    "Problem-solving mentor focused on DSA, coding rounds, and scalable service design.",
                    6.0,
                    4.6,
                    849.0,
                    "9090909090",
                    LocalDate.of(1994, 8, 5),
                    "Chennai",
                    List.of("Data Structures and Algorithms", "Java Backend Development", "System Design"),
                    skillIds);
        } catch (Exception ex) {
            log.warn("Indian mentor profile seeding skipped due to dependency/service readiness: {}", ex.getMessage());
        }
    }

    private Map<String, Long> fetchSkillIdsByName() {
        Map<String, Long> skillByName = new HashMap<>();
        ApiResponse<List<SkillDTO>> skillResponse = skillServiceClient.findAllSkills();
        List<SkillDTO> skills = skillResponse != null ? skillResponse.getData() : null;

        if (skills == null) {
            return skillByName;
        }

        for (SkillDTO skill : skills) {
            if (skill.getName() != null && skill.getId() != null) {
                skillByName.put(skill.getName().toLowerCase(), skill.getId());
            }
        }

        return skillByName;
    }

    private void seedMentor(
            String email,
            String bio,
            Double experience,
            Double rating,
            Double hourlyRate,
            String phone,
            LocalDate dob,
            String location,
            List<String> skillNames,
            Map<String, Long> skillIdsByName) {

        ApiResponse<UsersDTO> userResponse = userServiceClient.findUserByMail(email);
        UsersDTO user = userResponse != null ? userResponse.getData() : null;

        if (user == null || user.getId() == null) {
            log.warn("Skipping mentor profile seed because user not found: {}", email);
            return;
        }

        Mentor mentor = mentorRepository.findByUserId(user.getId()).orElseGet(Mentor::new);
        mentor.setUserId(user.getId());
        mentor.setBio(bio);
        mentor.setExperience(experience);
        mentor.setRating(rating);
        mentor.setHourlyRate(hourlyRate);
        mentor.setPhone(phone);
        mentor.setDob(dob);
        mentor.setLocation(location);
        mentor.setStatus("APPROVED");

        Mentor savedMentor = mentorRepository.save(mentor);

        for (String skillName : skillNames) {
            Long skillId = skillIdsByName.get(skillName.toLowerCase());
            if (skillId == null) {
                continue;
            }

            if (mentorSkillRepository.existsByMentorIdAndSkillId(savedMentor.getId(), skillId)) {
                continue;
            }

            MentorSkill mentorSkill = new MentorSkill();
            mentorSkill.setMentorId(savedMentor.getId());
            mentorSkill.setUserId(savedMentor.getUserId());
            mentorSkill.setSkillId(skillId);
            mentorSkillRepository.save(mentorSkill);
        }

        log.info("Seeded mentor profile for {}", email);
    }
}
