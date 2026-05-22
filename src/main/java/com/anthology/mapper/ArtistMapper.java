package com.anthology.mapper;

import com.anthology.dto.responses.ArtistResponse;
import com.anthology.model.Artist;
import com.anthology.model.ArtistRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistMapper {


    @Mapping(target = "id", ignore = true)
  ///  @Mapping(target = "id", ignore = true)
    Artist toEntity(ArtistRequest artistRequest);

    ArtistResponse toDTO(Artist artist);

}
