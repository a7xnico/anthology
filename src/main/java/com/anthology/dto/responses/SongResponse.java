package com.anthology.dto.responses;

import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SongResponse(
        Long id,
        String title,
        String artistName,
        String genre,
        String AlbumTitle,
        Year ReleaseYear,
        List<Instrument> availableInstruments,
        Status status
) {
}
