package com.anthology.dto.requests;

import com.anthology.enums.Instrument;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO utilizado para crear versiones de canciones en el sistema")
public record SongVersionRequest(
        @Schema(
                description = "Instrumento para el cual se creará la versión",
                example = "GUITAR"
        )
        @NotNull(message = "El instrumento no puede ser nulo")
        Instrument instrument
) {
}
