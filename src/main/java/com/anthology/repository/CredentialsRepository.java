package com.anthology.repository;

import com.anthology.model.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Long> {
    Optional<CredentialsEntity>findByUsername(String username);
}
