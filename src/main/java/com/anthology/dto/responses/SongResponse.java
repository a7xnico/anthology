package com.anthology.dto.responses;

import com.anthology.enums.Instrument;
import com.anthology.enums.Status;

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
        Status status,
        LocalDateTime createdAt
) {
}
