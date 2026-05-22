package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Year;

public record AlbumUpdateRequest(
        @Schema(
                description = "Título del álbum",
                example = "Selected Ambient Works 85-92"
        )
        String title,

        @Schema(
                description = "Año de lanzamiento del álbum",
                example = "1992"
        )
        Year releaseYear,

        @Schema(
                description = "Nombre del artista o banda",
                example = "Aphex Twin"
        )
        String artistName
) {
}
