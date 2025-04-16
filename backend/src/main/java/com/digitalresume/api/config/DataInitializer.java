package com.digitalresume.api.config;

import com.digitalresume.api.model.Role;
import com.digitalresume.api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default roles if they don't exist
        initRoles();
    }

    private void initRoles() {
        log.info("Initializing roles...");
        
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            log.info("No roles found, creating default roles");
            
            List<Role> defaultRoles = Arrays.asList(
                new Role("USER"),
                new Role("ADMIN"),
                new Role("MODERATOR")
            );
            
            roleRepository.saveAll(defaultRoles);
            log.info("Default roles created successfully: {}", defaultRoles);
        } else {
            log.info("Roles already exist in the database");
        }
    }
}