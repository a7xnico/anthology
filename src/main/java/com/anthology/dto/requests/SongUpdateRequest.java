package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO utilizado para modificar los atributos de una canción")
public record SongUpdateRequest(

        @Schema(
                description = "Nuevo título de la canción",
                example = "Xtal"
        )
        @NotBlank
        String title,
        @Schema(
                description = "Nuevo nombre del artista o banda",
                example = "Aphex Twin"
        )
        String artistName,

        @Schema(
                description = "Nuevo género musical",
                example = "Electrónica"
        )
        @Size(max = 100, message = "El género no puede superar los 100 caracteres")
        String genre,

        @Schema(
                description = "ID del álbum a asignar, null para desasignar",
                example = "2"
        )
        Long albumId
) {
}
