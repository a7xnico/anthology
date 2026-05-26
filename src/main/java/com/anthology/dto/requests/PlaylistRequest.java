package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO utilizado para crear playlist en el sistema")
public record PlaylistRequest(
        @Schema
                (
                        description = "nombre de la playlist",
                        example = "musica clasica"
                )
        @NotBlank(message = "el nombre del playlist no puede estar vacio")
        String name,
        @Schema
                (
                        description = "Id del usuario",
                        example = "1"
                )
        @NotNull(message = "el id del usuario no puede ser nulo")
        Long idUser


) {
}
