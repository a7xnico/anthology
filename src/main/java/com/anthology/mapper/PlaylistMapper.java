package com.anthology.mapper;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.model.Playlist;

public class PlaylistMapper {
    public Playlist toEntity(PlaylistRequest request)
    {
        return Playlist.builder().name(request.name()).build();
    }
    public PlaylistResponse toDto(Playlist playlist)
    {
        /// Tira error checkear
        /// return  PlaylistResponse.builder().id(playlist.getId()).name(playlist.getName()).songVersions(playlist.getSongVersions()).build();
     ///   return  PlaylistResponse.builder().id(playlist.getId()).name(playlist.getName()).build();
    }

}
