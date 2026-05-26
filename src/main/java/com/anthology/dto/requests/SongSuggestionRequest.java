package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO utilizado para sugerir agregar una cancion")
public record SongSuggestionRequest(
        @Schema(
                description = "el id del ususario",
                example = "3"
        )
        Long idUser,

        @Schema(
                description = "titulo de la cancion",
                example = "every breath you take"

        )
        @NotBlank(message = "el titulo de la cancion no puede ser nulo")
        String title,
        @Schema(
                description = "nombre del artista o banda",
                example = "the police"
        )
        @NotBlank(message = "el nombre del artista o banda no puede ser nulo")
        String artistName,
        @Schema(
                description = "la nota de la sugerencia ",
                example = "quiero sugerir esta cancion porque me encanta "
        )
        @NotBlank(message = "la nota no puede ser nula")
        String notes



) {
}
