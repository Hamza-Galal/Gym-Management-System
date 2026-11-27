package com.gym.GymManagementSystem.Config;

import com.gym.GymManagementSystem.Model.User;
import com.gym.GymManagementSystem.Model.Enums.Role;
import com.gym.GymManagementSystem.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Configuration
public class AdminInitializer {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initializeAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            if (adminEmail == null || adminEmail.isBlank()
                    || adminPassword == null || adminPassword.isBlank()) {

                throw new IllegalStateException( "Admin email and password must be configured");
            }

            String normalizedEmail = adminEmail
                    .trim()
                    .toLowerCase(Locale.ROOT);

            int passwordLength = adminPassword
                    .getBytes(StandardCharsets.UTF_8)
                    .length;

            if (passwordLength < 8 || passwordLength > 72) {
                throw new IllegalStateException("Admin password must be between 8 and 72 bytes");
             }

            var existingAdmin = userRepository.findByEmail(normalizedEmail);

            if (existingAdmin.isPresent()) {

                User user = existingAdmin.get();

                if (user.getRole() != Role.ADMIN) {
                    throw new IllegalStateException("Configured admin email belongs to a non-admin user");
                }
                return;
            }

            User admin = new User();

            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setEmail(normalizedEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));

            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setTokenVersion(0);

           
            userRepository.save(admin);
        };
    }
}