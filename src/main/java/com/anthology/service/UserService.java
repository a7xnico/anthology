package com.anthology.service;

import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.requests.UserUpdateRequest;
import com.anthology.dto.responses.UserResponse;
import com.anthology.enums.Role;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.UserMapper;
import com.anthology.model.CredentialsEntity;
import com.anthology.model.RoleEntity;
import com.anthology.model.Song;
import com.anthology.model.User;
import com.anthology.repository.CredentialsRepository;
import com.anthology.repository.RoleRepository;
import com.anthology.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponse createUser(UserRequest userRequest)
    {
        if(userRepository.existsByUsername(userRequest.username()))
            throw new DuplicateResourceException("Ya existe un usuario con ese nombre");

        if(userRepository.existsByEmailIgnoreCase(userRequest.email()))
            throw new DuplicateResourceException("Ya existe un usuario con ese email");

         User user=userMapper.toEntity(userRequest);

        String encodedPassword = passwordEncoder.encode(userRequest.password());

        user.setPasswordHash(encodedPassword);

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        User savedUser = userRepository.save(user);


        CredentialsEntity credentials = new CredentialsEntity();
        credentials.setUsername(savedUser.getUsername());
        credentials.setPassword(encodedPassword);
        credentials.setUser(savedUser);
        credentials.setRefreshToken(null);

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByRole(Role.USER).orElseThrow(()-> new ResourceNotFoundException("Rol no entontrado..."));

        roles.add(userRole);
        credentials.setRoles(roles);

        credentialsRepository.save(credentials);
        return userMapper.toDTO(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id,UserUpdateRequest request)
    {
        User user=findUserById(id);

        CredentialsEntity credentials = credentialsRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales del usuario no encontradas"));

        if (request.username() != null && !request.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.username()))
                throw new DuplicateResourceException("Ya existe un usuario con ese nombre");
            user.setUsername(request.username());
            credentials.setUsername(request.username());
        }

        if (request.email() != null && !request.email().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmailIgnoreCase(request.email()))
                throw new DuplicateResourceException("Ya existe un usuario con ese email");
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.password());
            user.setPasswordHash(encodedPassword);
            credentials.setPassword(encodedPassword);
        }

        userRepository.save(user);
        credentialsRepository.save(credentials);

        return userMapper.toDTO(user);
    }

    public User findUserById(Long id)
    {
        return userRepository
                .findById(id)
                .filter(user -> !user.isDeleted())
                .orElseThrow(()-> new ResourceNotFoundException("user no encontrado"));
    }

    public List<UserResponse> findAllUsers()
    {
        return userRepository
                .findByDeletedAtIsNull()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public List<UserResponse>findAllUsersByUser(){
        return userRepository
                .findByRoleNotAndDeletedAtIsNull(Role.ADMIN)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }


    public UserResponse findById(Long id)
    {
        return userMapper.toDTO(findUserById(id));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user=findUserById(id);

        CredentialsEntity credentials = credentialsRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales del usuario no encontradas"));

        String deletedUsername = liberarUsername(user.getUsername(), id);
        String deletedEmail = liberarEmail(user.getEmail(), id);

        user.setUsername(deletedUsername);
        user.setEmail(deletedEmail);
        user.softDelete();
        userRepository.save(user);

        credentials.setUsername(deletedUsername);
        credentials.setRefreshToken(null);
        credentialsRepository.save(credentials);
    }

    private String liberarUsername(String username, Long id) {
        String sufijo = "_deleted_" + id + "_" + System.currentTimeMillis();
        int maxBase = 50 - sufijo.length();
        if (maxBase < 0) maxBase = 0;

        String base = username.length() > maxBase ? username.substring(0, maxBase) : username;
        return base + sufijo;
    }

    private String liberarEmail(String email, Long id) {
        String sufijo = ".deleted." + id + "." + System.currentTimeMillis();
        int arroba = email.lastIndexOf('@');

        if (arroba <= 0) return email + sufijo;

        return email.substring(0, arroba) + sufijo + email.substring(arroba);
    }
}
