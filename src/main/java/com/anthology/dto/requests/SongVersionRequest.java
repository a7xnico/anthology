package com.anthology.dto.requests;

import com.anthology.enums.Instrument;
import jakarta.validation.constraints.NotNull;

public record SongVersionRequest(
        @NotNull
        Instrument instrument
) {
}
