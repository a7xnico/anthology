package com.anthology.dto.JwtDTO;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank String username,
        @NotBlank String password
) {

}
