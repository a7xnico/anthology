package com.anthology.dto.responses;

import com.anthology.enums.Role;
import com.google.api.client.util.DateTime;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Builder

public record UserResponse(
        String username,
     String email,
     Role role,
     LocalDateTime createdAt
)
{
}
