package com.anthology.dto.responses;

import com.anthology.enums.Role;
import lombok.Builder;
import java.time.LocalDateTime;
@Builder

public record UserResponse(

        String userName,
     String email,
     Role role,
     LocalDateTime createdAt
)
{
}
