package com.anthology.mapper;


import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.enums.Instrument;
import com.anthology.model.Song;
import com.anthology.model.SongVersion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(target = "album", ignore = true)
    Song toEntity(SongRequest songRequest);

    @Mapping(target = "albumTitle", source = "album.title")
    @Mapping(target = "releaseYear", source = "album.releaseYear")
    @Mapping(target = "availableInstruments", source = "songVersions")
    SongResponse toDTO(Song song);

    default List<Instrument> mapInstruments(List<SongVersion> versions) {
        return versions.stream()
                .map(SongVersion::getInstrument)
                .toList();
    }
}
