package com.lpu.AuthService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IndianMentorUserSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IndianMentorUserSeeder.class);

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedMentorUser("arjun.sharma@skillsync.com", "Arjun", "Sharma");
        seedMentorUser("priya.nair@skillsync.com", "Priya", "Nair");
        seedMentorUser("rahul.verma@skillsync.com", "Rahul", "Verma");
        seedMentorUser("neha.iyer@skillsync.com", "Neha", "Iyer");
    }

    private void seedMentorUser(String email, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        User mentor = new User();
        mentor.setEmail(email);
        mentor.setPassword(passwordEncoder.encode("Mentor@123!"));
        mentor.setRole("ROLE_MENTOR");
        mentor.setFirstName(firstName);
        mentor.setLastName(lastName);
        mentor.setName(firstName + " " + lastName);
        mentor.setActive(true);
        mentor.setVerified(true);
        mentor.setProviders(new ArrayList<>(List.of("LOCAL")));

        userRepository.save(mentor);
        log.info("Seeded mentor user: {}", email);
    }
}
