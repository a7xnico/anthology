package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Year;

@Schema(description = "DTO para crear o actualizar un álbum")
public record AlbumRequest(
        @Schema(
                description = "Título del álbum",
                example = "Abbey Road"
        )
        @NotBlank(message = "El título no puede estar vacío")
        String title,

        @Schema(
                description = "Año de lanzamiento del álbum",
                example = "1969"
        )
        @NotNull
        @PastOrPresent
        Year releaseYear,

        @Schema(
                description = "Nombre del artista o banda",
                example = "The Beatles"
        )
        @NotBlank(message = "El nombre del artista no puede estar vacío")
        String artistName
) {
}
