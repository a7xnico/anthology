package com.anthology.repository;

import com.anthology.enums.Role;
import com.anthology.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmailIgnoreCase(String email);
    List<User> findByRoleNot(Role role);
    List<User> findByDeletedAtIsNull();
    List<User> findByRoleNotAndDeletedAtIsNull(Role role);
}
