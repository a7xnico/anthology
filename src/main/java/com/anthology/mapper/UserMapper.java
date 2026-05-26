package com.anthology.mapper;

import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.responses.UserResponse;
import com.anthology.model.User;

public class UserMapper {

    public User toEntity(UserRequest dto)
    {
        return User.builder().username(dto.username()).email(dto.email()).passwordHash(dto.password()).build();
    }

    public UserResponse toDto(User user)
    {
        return UserResponse.builder().userName(user.getUsername()).email(user.getEmail()).role(user.getRole()).createdAt(user.getCreatedAt()).build();
    }

}
