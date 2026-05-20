package com.anthology.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SongRequest(
        @NotBlank
        String title,

        @NotBlank
        String artistName,

        @NotBlank
        @Size(max = 100)
        String genre
) {
}
