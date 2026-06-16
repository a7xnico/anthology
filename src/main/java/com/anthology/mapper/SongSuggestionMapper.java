package com.anthology.mapper;

import com.anthology.dto.requests.SongSuggestionRequest;
import com.anthology.dto.responses.SongSuggestionResponse;
import com.anthology.model.SongSuggestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongSuggestionMapper {
    SongSuggestion toEntity(SongSuggestionRequest dto);
    @Mapping(source = "user.id", target = "idUser")
    SongSuggestionResponse toDTO(SongSuggestion entity);
}
