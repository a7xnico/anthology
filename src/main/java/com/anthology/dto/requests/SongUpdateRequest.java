package com.anthology.dto.requests;

import jakarta.validation.constraints.Size;

public record SongUpdateRequest(

        String title,

        String artistName,

        @Size(max = 100)
        String genre
) {
}
