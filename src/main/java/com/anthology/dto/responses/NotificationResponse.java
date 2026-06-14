package com.anthology.dto.responses;

import com.anthology.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String message,
        Boolean read,
        NotificationType type,
        LocalDateTime createdAt
) {
}
