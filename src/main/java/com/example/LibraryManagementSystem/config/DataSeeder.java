package com.example.LibraryManagementSystem.config;

import com.example.LibraryManagementSystem.enums.Role;
import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void run(String... args) throws Exception {
        // Check if an admin user already exists
        boolean adminExists = userRepository.existsByUsername("admin");
        if (!adminExists) {
            // Create a new admin user
            User admin = User.builder()
                    .firstName("Default")
                    .initial("A")
                    .lastName("Admin")
                    .username("admin")
                    .password(passwordEncoder.encode("adminpassword")) // Secure password
                    .role(Role.ROLE_ADMIN)
                    .build();

            // Save the admin user to the database
            userRepository.save(admin);

            System.out.println("Default admin user created with username 'admin' and password 'adminpassword'.");
            // Optionally, you can generate a JWT token for immediate use
            String token = jwtService.generateToken(admin);
            System.out.println("Admin JWT Token: " + token);
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
