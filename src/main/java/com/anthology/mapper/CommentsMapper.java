package com.anthology.mapper;

import com.anthology.dto.requests.CommentsRequest;
import com.anthology.dto.responses.CommentsResponse;
import com.anthology.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentsMapper {
    Comment toEntity(CommentsRequest request);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "songTitle", source = "songVersion.song.title")
    @Mapping(target = "instrument", source = "songVersion.instrument")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    CommentsResponse toDto(Comment comment);
}
