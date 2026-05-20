package com.anthology.mapper;


import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.model.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song toEntity(SongRequest songRequest);
    SongResponse toDTO(Song song);
}
