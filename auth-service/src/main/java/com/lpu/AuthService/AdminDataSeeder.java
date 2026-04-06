package com.lpu.AuthService;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;

@Component
public class AdminDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminDataSeeder.class);

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataSeeder(AuthUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdminUser("admin1@skillsync.com", "Admin@123!", "adminA", "One");
        seedAdminUser("admin2@skillsync.com", "Admin@123!", "adminB", "Two");
        seedAdminUser("admin3@skillsync.com", "Admin@123!", "adminC", "Three");
    }

    private void seedAdminUser(String email, String password,
                               String firstName, String lastName) {

        if (!userRepository.existsByEmail(email)) {

            User admin = new User();

            // ✅ Required fields
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole("ROLE_ADMIN");
            admin.setFirstName(firstName);
            admin.setLastName(lastName);

            // ✅ Optional but recommended
            admin.setName(firstName + " " + lastName);
            admin.setActive(true);
            admin.setVerified(true);

            // 🔥 IMPORTANT (for consistency with OAuth)
            admin.setProviders(new ArrayList<>());
            admin.getProviders().add("LOCAL");
            userRepository.save(admin);

            log.info("✅ Admin created: {}", email);

        } else {
            log.debug("ℹ️ Admin already exists: {}", email);
        }
    }
}