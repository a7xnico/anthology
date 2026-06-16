package com.anthology.dto.JwtDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para autenticar un usuario en el sistema")
public record AuthRequestDTO(
        @Schema(
                description = "Nombre de usuario",
                example = "Juan"
        )
        @NotBlank(message = "El username no puede estar vacío")
        String username,
        @Schema(
                description = "Contraseña del usuario",
                example = "Juansito555"
        )
        @NotBlank(message = "La contraseña no puede estar vacía")
        String password
) {

}
