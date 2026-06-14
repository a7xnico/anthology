package com.anthology.dto.responses;

import com.anthology.enums.Instrument;
import com.anthology.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SongVersionResponse(
        Long id,
        Long songId,
        Instrument instrument,
        Status status,
        String pdfUrl,
        LocalDateTime createdAt
) {
}
