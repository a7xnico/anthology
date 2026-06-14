package com.anthology.dto.responses;

import com.anthology.enums.Status;

import java.time.LocalDateTime;
import java.time.Year;

public record AlbumResponse(
        Long id,
        String title,
        String artistName,
        Year releaseYear,
        Status status,
        LocalDateTime createdAt
) {
}
