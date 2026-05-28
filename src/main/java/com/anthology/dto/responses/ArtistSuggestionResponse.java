package com.anthology.dto.responses;

import com.anthology.enums.Status;

public record ArtistSuggestionResponse(


        Long id,
        String stageName,
        String biography,
        Status status
)
{}
