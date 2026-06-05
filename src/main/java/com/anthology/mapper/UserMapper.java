package com.anthology.mapper;

import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.responses.UserResponse;
import com.anthology.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest dto);
    // ignore a las playlist
    // ignore a la contraseña
    // ignore al rol
    UserResponse toDTO(User Entity);
}
