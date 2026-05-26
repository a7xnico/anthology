package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Schema(description = "DTO utilizado para modificar los atributos de un usuario")
public record UserUpdateRequest(
        @Schema(
                description = "nombre del usuario",
                example = "carlos485"
        )
        @NotBlank(message = "el nombre del usuario no puede estar vacio")
        String username,

        @Schema(
                description = "email del usuario",
                example = "carlos@gmail.com"

        )
        @NotBlank(message = "el email no puede estar vacio")
        @Email(message = "el email no cumple con el formato correcto")
        String email,
        @Schema(
                description = "contraseña del usuario",
                example = "Carlos123"

        )
        @NotBlank(message = "la contraseña no puede estar vacia")
        String password
)
{
}
