package com.anthology.dto.responses;



import com.anthology.enums.Instrument;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CommentsResponse(

        Long id,
        String username,
        String songTitle,
        Instrument instrument,
        String content,
        String createdAt
) {
}
