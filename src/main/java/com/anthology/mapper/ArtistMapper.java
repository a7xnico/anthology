package com.anthology.mapper;

import com.anthology.dto.requests.ArtistRequest;
import com.anthology.dto.responses.ArtistResponse;
import com.anthology.model.Artist;
import com.anthology.model.ArtistSuggestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "id", ignore = true)
    Artist toEntity(ArtistRequest artistRequest);

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    ArtistResponse toDTO(Artist artist);
}
