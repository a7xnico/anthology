package com.anthology.config;

import com.anthology.enums.Role;
import com.anthology.model.RoleEntity;
import com.anthology.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

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
    }
}