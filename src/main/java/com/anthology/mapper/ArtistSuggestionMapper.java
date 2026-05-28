package com.anthology.mapper;

import com.anthology.dto.requests.ArtistSuggestionRequest;
import com.anthology.dto.responses.ArtistSuggestionResponse;
import com.anthology.model.Artist;
import com.anthology.model.ArtistSuggestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistSuggestionMapper {

    @Mapping(target = "id", ignore = true)
    ///  @Mapping(target = "id", ignore = true)
    ArtistSuggestion toEntity(ArtistSuggestionRequest artistSuggestionRequest);

    ArtistSuggestionResponse toDTO(ArtistSuggestion artistSuggestion);
}
