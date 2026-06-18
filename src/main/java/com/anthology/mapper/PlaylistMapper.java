package com.anthology.mapper;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.model.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SongVersionMapper.class})
public interface PlaylistMapper {

    Playlist toEntity(PlaylistRequest dto);
    @Mapping(source = "user.id", target = "idUser")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    PlaylistResponse toDTO(Playlist playlist);
}
