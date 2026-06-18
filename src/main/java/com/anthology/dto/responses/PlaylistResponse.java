package com.anthology.dto.responses;

import com.anthology.model.SongVersion;
import com.anthology.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


public record PlaylistResponse(
         Long id,
         Long idUser,
         String name,
         boolean isDefault,
         LocalDateTime createdAt,
         List<SongVersionResponse> songVersions


) {
}
