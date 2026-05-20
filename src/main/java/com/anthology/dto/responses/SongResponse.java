package com.anthology.dto.responses;

import java.time.LocalDateTime;

public record SongResponse(
        Long id,
        String title,
        String artistName,
        String genre,
        LocalDateTime createdAt
) {
}
