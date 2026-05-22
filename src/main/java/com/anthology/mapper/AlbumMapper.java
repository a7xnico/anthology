package com.anthology.mapper;

import com.anthology.dto.requests.AlbumRequest;
import com.anthology.dto.responses.AlbumResponse;
import com.anthology.model.Album;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AlbumMapper {

    Album toEntity(AlbumRequest request);

    AlbumResponse toDTO(Album album);

}
