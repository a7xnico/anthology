package com.anthology.controller;

import com.anthology.dto.responses.NotificationResponse;
import com.anthology.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del administrador")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Listar notificaciones", description = "Devuelve todas las notificaciones ordenadas por fecha")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> findAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @Operation(summary = "Notificaciones no leídas", description = "Devuelve solo las notificaciones no leídas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/unread")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> findUnread() {
        return ResponseEntity.ok(notificationService.findUnread());
    }

    @Operation(summary = "Marcar como leída", description = "Marca una notificación específica como leída")
    @ApiResponse(responseCode = "200", description = "Notificación marcada como leída")
    @PatchMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "ID de la notificación") @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @Operation(summary = "Marcar todas como leídas", description = "Marca todas las notificaciones como leídas")
    @ApiResponse(responseCode = "204", description = "Todas las notificaciones marcadas como leídas")
    @PatchMapping("/read-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }
}
