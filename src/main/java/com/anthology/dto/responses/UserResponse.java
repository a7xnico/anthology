package com.anthology.dto.responses;

import com.anthology.enums.Role;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


public record UserResponse(
        Long id,
        String username,
        String email,
        Role role
) {
}
