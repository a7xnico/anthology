package com.anthology.config;

import com.anthology.enums.Role;
import com.anthology.model.CredentialsEntity;
import com.anthology.model.RoleEntity;
import com.anthology.model.User;
import com.anthology.repository.CredentialsRepository;
import com.anthology.repository.RoleRepository;
import com.anthology.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        for (Role role : Role.values()) {
            if (roleRepository.findByRole(role).isEmpty()) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setRole(role);
                roleRepository.save(roleEntity);
            }
        }
        System.out.println("Roles inicializados: " + roleRepository.findAll());

        if (!userRepository.existsByUsername("ADMIN001")) {
            User admin = new User();
            admin.setUsername("ADMIN001");
            admin.setEmail("admin@gmail.com");
            admin.setPasswordHash(passwordEncoder.encode("Admin"));
            admin.setRole(Role.ADMIN);
            User savedAdmin = userRepository.save(admin);

            RoleEntity adminRole = roleRepository.findByRole(Role.ADMIN)
                    .orElseThrow();

            CredentialsEntity credentials = new CredentialsEntity();
            credentials.setUsername("ADMIN001");
            credentials.setPassword(passwordEncoder.encode("Admin"));
            credentials.setUser(savedAdmin);
            credentials.setRefreshToken(null);
            credentials.setRoles(new HashSet<>(Set.of(adminRole)));
            credentialsRepository.save(credentials);

            System.out.println("Admin creado: ADMIN001 / Admin");
        }
    }
}