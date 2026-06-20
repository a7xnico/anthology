package com.anthology.mapper;

import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.model.SongVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongVersionMapper {
    @Mapping(target = "songId", source = "song.id")
    @Mapping(target = "songTitle", source = "song.title")
    SongVersionResponse toDTO(SongVersion songVersion);
}
