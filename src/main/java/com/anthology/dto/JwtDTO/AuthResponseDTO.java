package com.anthology.dto.JwtDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta al autenticar un usuario")
public record AuthResponseDTO(
        @Schema(
                description = "Token de acceso JWT",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String AccessToken,

        @Schema(
                description = "Token de refresco para renovar el access token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String refreshToken
) {
}
