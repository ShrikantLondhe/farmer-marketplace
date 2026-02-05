package com.farmermarket.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminDataLoader {

    @Bean
    public CommandLineRunner loadAdmin(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {

        return args -> {

            // check if admin already exists
            if (userRepository.findByUsername("admin@gmail.com").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin@123"));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);

                userRepository.save(admin);

                System.out.println("✅ DEFAULT ADMIN CREATED : admin@gmail.com / admin@123");
            }
        };
    }
}
