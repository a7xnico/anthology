package com.anthology.mapper;

import com.anthology.dto.responses.NotificationResponse;
import com.anthology.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toDTO(Notification notification);
}
