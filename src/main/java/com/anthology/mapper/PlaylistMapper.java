package com.anthology.mapper;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.model.Playlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    Playlist toEntity(PlaylistRequest dto);
    PlaylistResponse toDTO(Playlist playlist);
}
