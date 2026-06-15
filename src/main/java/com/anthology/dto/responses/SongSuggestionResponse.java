package com.anthology.dto.responses;

import com.anthology.enums.SongSuggestionStatus;
import com.anthology.enums.Status;
import lombok.Builder;

import java.time.LocalDateTime;


public record SongSuggestionResponse(
        Long id,
        Long idUser,
        String title,
        String artistName,
        String notes,
        SongSuggestionStatus status,
        LocalDateTime createdAt

) {
}
