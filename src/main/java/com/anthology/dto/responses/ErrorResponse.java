package com.anthology.dto.responses;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String message
) {
}
