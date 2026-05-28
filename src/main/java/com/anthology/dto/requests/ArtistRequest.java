package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "DTO utilizado para crear Artistas en el sistema")
public record ArtistRequest (

        @Schema(
                description = "Nombre del artista/s",
                example = "Bruno Mars"
        )
        @NotBlank(message = "El nombre no puede estar vacio")
         String stageName,

        @Schema(
                description = "Biografia del Artista",
                example = "Bruno Mars de origen Americano es un..."
        )
        @NotBlank(message = "La biografia no puede estar vacia")
         String biography,

        @Schema(
                description = "URL Instagram del Artista",
                example = "https://www.instagram.com/BrunoMars"
        )
         String instagram,

        @Schema(
                description = "URL Spotify del Artista",
                example = "https://www.spotify.com/BrunoMars"
        )
         String spotify,

        @Schema(
                description = "URL Youtube del Artista",
                example = "https://www.youtube.com/@brunomars"
        )
         String youtube,

        @Schema(
                description = "ID User",
                example = "1"
        )
        Long userId

){}
