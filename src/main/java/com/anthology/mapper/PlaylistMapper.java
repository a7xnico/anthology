package com.anthology.mapper;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.model.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    Playlist toEntity(PlaylistRequest dto);
    @Mapping(source = "user.id", target = "idUser")
    PlaylistResponse toDTO(Playlist playlist);
}
