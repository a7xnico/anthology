package com.anthology.service;

import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.requests.UserUpdateRequest;
import com.anthology.dto.responses.UserResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.UserMapper;
import com.anthology.model.User;
import com.anthology.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserResponse createUser(UserRequest userRequest)
    {
        if(userRepository.exitsByUserName(userRequest.username()))
            throw new DuplicateResourceException("Ya existe un usuario con ese nombre");
         User user=userMapper.toEntity(userRequest);
         return userMapper.toDto(userRepository.save(user));
    }

    public UserResponse updateUser(Long id,UserUpdateRequest request)
    {
        User user=findUserById(id);
        if(request.username()!= null)user.setUsername(request.username());
        if(request.email()!= null)user.setEmail(request.email());
        if(request.password()!= null)user.setPasswordHash(request.password());

        return userMapper.toDto(userRepository.save(user));
    }

    public User findUserById(Long id)
    {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("user no encontrado"));
    }

    public List<UserResponse> findAllUsers()
    {
        return userRepository.findAll().stream().map(userMapper::toDto).toList;

    }
    public UserResponse findById(Long id)
    {
        return userMapper.toDto(findUserById(id));
    }
    public void deleateUser(Long id)
    {
        User user=findUserById(id);
        userRepository.delete(user);
    }
}
