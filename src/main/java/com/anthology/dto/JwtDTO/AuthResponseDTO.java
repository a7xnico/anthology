package com.anthology.dto.JwtDTO;

public record AuthResponseDTO(
        String AccessToken, String refreshToken
) {
}
