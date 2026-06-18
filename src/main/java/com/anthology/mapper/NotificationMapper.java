package com.anthology.mapper;

import com.anthology.dto.responses.NotificationResponse;
import com.anthology.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    NotificationResponse toDTO(Notification notification);
}
