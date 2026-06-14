package com.anthology.service;

import com.anthology.dto.responses.NotificationResponse;
import com.anthology.enums.NotificationType;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.NotificationMapper;
import com.anthology.model.Notification;
import com.anthology.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public void create(String message, NotificationType type){
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType(type);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> findAll() {
        return notificationRepository.findAllByOrderCreatedAtDesc()
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    public List<NotificationResponse> findUnread() {
        return notificationRepository.findByReadFalseOrderByCreatedAtDesc()
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
        notification.setRead(true);
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    public void markAllAsRead() {
        List<Notification> unread = notificationRepository.findByReadFalseOrderByCreatedAtDesc();
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

}
