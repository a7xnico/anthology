package com.anthology.dto.responses;

import com.anthology.enums.Instrument;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public record SongResponse(
        Long id,
        String title,
        String artistName,
        String genre,
        String AlbumTitle,
        Year ReleaseYear,
        List<Instrument> availableInstruments,
        LocalDateTime createdAt
) {
}
