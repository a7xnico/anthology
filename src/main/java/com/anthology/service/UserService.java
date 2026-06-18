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

         User user=userMapper.toEntity(userRequest);

        String encodedPassword = passwordEncoder.encode(userRequest.password());

        user.setPasswordHash(passwordEncoder.encode(encodedPassword));

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

    public UserResponse updateUser(Long id,UserUpdateRequest request)
    {
        User user=findUserById(id);
        if(request.username()!= null)user.setUsername(request.username());
        if(request.email()!= null)user.setEmail(request.email());
        if (request.password() != null) user.setPasswordHash(
                passwordEncoder.encode(request.password()));

        return userMapper.toDTO(userRepository.save(user));
    }

    public User findUserById(Long id)
    {
        return userRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user no encontrado"));
    }

    public List<UserResponse> findAllUsers()
    {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public List<UserResponse>findAllUsersByUser(){
        return userRepository
                .findByRoleNot(Role.ADMIN)
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

        ///BORRAR CREDENCIALES
        credentialsRepository.findByUsername(user.getUsername())
                .ifPresent(credentialsRepository::delete);


        userRepository.delete(user);
    }

    // utilizar borrado logico
//    public void softDeleteUser(Long id){
//        User user = findUserById(id);
//        user.softDelete();
//        userRepository.save(user);
//    }
}
