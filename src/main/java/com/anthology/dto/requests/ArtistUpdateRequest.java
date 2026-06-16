package com.anthology.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record ArtistUpdateRequest(
        @NotBlank(message = "la biografia del artista no puede esta vacia")
        String biography,
        @NotBlank(message = "el instagram del artista no puede estar vacio")
        String instagram,
        @NotBlank(message = "el spotify del artista no puede estar vacio")
        String spotify,
        @NotBlank(message = "el youtube del artista no puede esta vacio")
        String youtube) {
}
