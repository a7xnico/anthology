package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO utilizado para crear canciones en el sistema")
public record SongRequest(
        @Schema(
                description = "Título de la canción",
                example = "Come Together"
        )
        @NotBlank(message = "El título no puede estar vacío")
        String title,

        @Schema(
                description = "Nombre del artista o banda",
                example = "The Beatles"
        )
        @NotBlank(message = "El nombre del artista no puede estar vacío")
        String artistName,

        @Schema(
                description = "Género musical",
                example = "Rock"
        )
        @NotBlank(message = "El género no puede estar vacío")
        @Size(max = 100, message = "El género no puede superar los 100 caracteres")
        String genre,

        @Schema(
                description = "ID del álbum al que pertenece la canción, opcional",
                example = "1"
        )
        Long albumId
) {
}
