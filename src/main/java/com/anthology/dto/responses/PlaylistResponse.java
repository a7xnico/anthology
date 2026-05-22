package com.anthology.dto.responses;

import com.anthology.model.SongVersion;
import com.anthology.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
@Builder

public record PlaylistResponse(
         Long id,
         String name,
         boolean isDefault,
         LocalDateTime createdAt


) {
}
