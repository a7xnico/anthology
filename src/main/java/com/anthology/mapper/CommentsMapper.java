package com.anthology.mapper;

import com.anthology.dto.requests.CommentsRequest;
import com.anthology.dto.responses.CommentsResponse;
import com.anthology.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentsMapper {
    public Comment toEntity(CommentsRequest dto)
    {
        return Comment.builder().content(dto.content()).build();
    }
    public CommentsResponse toDto(Comment comment)
    {
       return CommentsResponse.builder().id(comment.getId()).idUser(comment.getUser().getId()).idSongVersion(comment.getSongVersion().getId()).content(comment.getContent()).createdAt(comment.getCreatedAt()).build();
    }
}
