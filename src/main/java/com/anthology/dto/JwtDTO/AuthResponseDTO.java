package com.anthology.dto.JwtDTO;

public record AuthResponseDTO(
        String tokenType,
        String accessToken
) {
}
