package com.anthology.mapper;

import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.model.SongVersion;
import com.anthology.service.CloudinaryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CloudinaryService.class)
public interface SongVersionMapper {
    @Mapping(target = "songId", source = "song.id")
    @Mapping(target = "songTitle", source = "song.title")
    @Mapping(target = "pdfUrl", source = "pdfPublicId", qualifiedByName = "fileUrl")
    SongVersionResponse toDTO(SongVersion songVersion);
}
