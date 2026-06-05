package com.anthology.dto.responses;



import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CommentsResponse(

        Long id,
        Long idUser,
        Long idSongVersion,
        String content,
        LocalDateTime createdAt
) {
}
