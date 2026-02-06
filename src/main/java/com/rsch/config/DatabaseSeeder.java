package com.rsch.config;

import com.rsch.model.Role;
import com.rsch.model.User;
import com.rsch.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {

                User admin = new User();
                admin.setName("Rodrigo");
                admin.setLastName("Schillaci");
                admin.setUsername("admin");
                admin.setEmail("admin@test.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("user ADMIN created: admin / admin123");
            }
        };
    }
}