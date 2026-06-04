package com.anthology.mapper;

import com.anthology.dto.requests.SongSuggestionRequest;
import com.anthology.dto.responses.SongSuggestionResponse;
import com.anthology.model.SongSuggestion;

public class SongSuggestionMapper {
    public SongSuggestion toEntity(SongSuggestionRequest dto)
    {
        return SongSuggestion.builder().notes(dto.notes()).title(dto.title()).artistName(dto.artistName()).build();
    }
    public SongSuggestionResponse toDto(SongSuggestion songSuggestion)
    {
        return SongSuggestionResponse.builder().notes(songSuggestion.getNotes()).id(songSuggestion.getId()).title(songSuggestion.getTitle()).idUser(songSuggestion.getUser().getId()).artistName(songSuggestion.getArtistName()).status(songSuggestion.getStatus()).createdAt(songSuggestion.getCreatedAt()).build();
    }
}
