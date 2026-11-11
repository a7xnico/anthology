package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PlaylistUpdateRequest(
        @Schema
                (
                        description = "nombre de la playlist",
                        example = "musica clasica"
                )
        @NotBlank(message = "el nombre del playlist no puede estar vacio")
        String name
) {
}
