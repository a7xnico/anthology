package com.anthology.dto.responses;

import java.time.LocalDateTime;

public record ArtistResponse(
        Long id,
        String stageName,
        String biography,
        String instagram,
        String youtube,
        String spotify,
        String createdAt
) {
}
