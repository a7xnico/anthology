package com.anthology.mapper;


import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.model.Song;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song toEntity(SongRequest songRequest);
    SongResponse toDTO(Song song);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(SongUpdateRequest request, @MappingTarget Song song);
}
