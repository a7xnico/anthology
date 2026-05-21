package com.anthology.dto.responses;

import java.time.LocalDateTime;
import java.time.Year;

public record AlbumResponse(
        Long id,
        String title,
        String artistName,
        Year releaseYear,
        LocalDateTime createdAt
) {
}
