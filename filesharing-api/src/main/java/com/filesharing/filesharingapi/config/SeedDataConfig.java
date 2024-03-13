package com.filesharing.filesharingapi.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.filesharing.filesharingapi.repository.UserRepository;
import com.filesharing.filesharingapi.enums.Role;
import com.filesharing.filesharingapi.entity.User;
import com.filesharing.filesharingapi.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Configuration component responsible for seeding the application with initial data upon startup.
 * Specifically, this class ensures that default admin users are created if the user repository is empty.
 * This is crucial for enabling immediate access to the application with administrative privileges.
 *
 * <p>Utilizes Spring Boot's {@link CommandLineRunner} to execute the seeding after the application context is fully loaded.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    /**
     * Seeds the application with default admin users if none exist. This method is invoked automatically at application startup.
     *
     * @param args command line arguments passed to the application, not used in this method.
     */
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.debug("No existing users found. Starting the seeding process.");

            User admin = User.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userService.save(admin);
            log.debug("Created ADMIN user - {}", admin);

            User admin2 = User.builder()
                    .username("admin2")
                    .email("admin2@admin2.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userService.save(admin2);
            log.debug("Created second ADMIN user - {}", admin2);

            log.info("Seeding process completed successfully. Default admin users are created.");
        } else {
            // This branch indicates users are already present, and no action is taken.
            log.debug("User repository is not empty. Skipping the seeding process.");
        }
    }
}