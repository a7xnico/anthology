package com.anthology.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO utilizado para crear comentarios")
public record CommentsRequest(
   @Schema(

           description = "ID del usuario",
           example = "1"
   )
   @NotNull(message = "el ID del usuario no puede ser nulo")
   Long idUser,
   @Schema(

           description = "ID de la songVersion",
           example = "3"
   )
   @NotNull(message = "el ID de la version de la cancion no puede ser nula")
   Long SongVersion ,
   @Schema(

           description = "contenido del comentario",
           example = "me encanta esta cancion"
   )
   @NotBlank(message = "el contenido no puede estar vacio")
   String content
) {
}
