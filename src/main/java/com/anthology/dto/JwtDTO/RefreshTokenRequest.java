package com.anthology.dto.JwtDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para renovar el token de acceso")
public record RefreshTokenRequest(

        @Schema(
                description = "Token de refresco válido",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @NotBlank(message = "El refresh token no puede estar vacío")
        String refreshToken)
{
}
