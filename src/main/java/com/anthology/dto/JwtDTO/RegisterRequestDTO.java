package com.anthology.dto.JwtDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para registrar un nuevo usuario en el sistema")
public record RegisterRequestDTO (
        @Schema(
                description = "Nombre de usuario único",
                example = "carlos485"
        )
        @NotBlank(message = "El username es obligatorio")
        String username,

        @Schema(
                description = "Contraseña del usuario, mínimo 4 caracteres",
                example = "Carlos123"
        )
        @NotBlank(message = "La password es obligatoria")
        @Size(min = 4, message = "La password debe tener al menos 4 caracteres")
        String password
){
}
