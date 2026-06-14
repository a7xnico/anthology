package com.anthology.controller;

import com.anthology.dto.requests.AlbumRequest;
import com.anthology.dto.requests.AlbumUpdateRequest;
import com.anthology.dto.responses.AlbumResponse;
import com.anthology.enums.Status;
import com.anthology.model.CredentialsEntity;
import com.anthology.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@AllArgsConstructor
@Tag(name = "Álbumes", description = "Gestión y Consulta de álbumes")
public class AlbumController {
    private final AlbumService albumService;

    @Operation(summary = "Crear álbum", description = "Crea un nuevo álbum en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un álbum con ese título y artista")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody AlbumRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(albumService.createAlbum(request));
    }

    @Operation(summary = "Crear álbum como artista", description = "Crea un nuevo álbum pendiente de aprobación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "403", description = "No tenés perfil de artista"),
            @ApiResponse(responseCode = "409", description = "Ya existe un álbum con ese título y artista")
    })
    @PostMapping("/artist")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<AlbumResponse> createAlbumAsArtist(
            @Valid @RequestBody AlbumRequest request,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(albumService.createAlbumAsArtist(request, credentials.getUser().getId()));
    }

    @Operation(summary = "Editar álbum", description = "Modifica parcialmente los datos de un álbum existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Álbum no encontrado")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @Parameter(description = "ID del álbum") @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.updateAlbum(id, request));
    }

    @Operation(summary = "Eliminar álbum", description = "Elimina un álbum del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Álbum eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Álbum no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAlbum(
            @Parameter(description = "ID del álbum") @PathVariable Long id){
        albumService.deleteAlbum(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlbumResponse> updateStatus(
            @Parameter(description = "ID del álbum") @PathVariable Long id,
            @Parameter(description = "Nuevo estado") @RequestParam Status status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.updateStatus(id, status));
    }

    @Operation(summary = "Listar álbumes", description = "Devuelve todos los álbumes disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de álbumes obtenida exitosamente")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<AlbumResponse>> findAllAlbums() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.findAllAlbums());
    }

    @Operation(summary = "Detalle de álbum", description = "Devuelve el detalle de un álbum por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "álbum encontrado"),
            @ApiResponse(responseCode = "404", description = "álbum no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> findById(
            @Parameter(description = "ID del álbum") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.findById(id));
    }

    @Operation(summary = "Mis álbumes", description = "Devuelve todos los álbumes del artista autenticado con su estado")
    @ApiResponse(responseCode = "200", description = "Lista de álbumes obtenida exitosamente")
    @GetMapping("/my-albums")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<AlbumResponse>> findMyAlbums(
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.findMyAlbums(credentials.getUser().getId()));
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlbumResponse>> findByStatus(
            @RequestParam Status status) {
        return ResponseEntity.ok(albumService.findByStatus(status));
    }

    @Operation(summary = "Editar álbum como artista", description = "Modifica parcialmente un álbum propio pendiente de aprobación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "403", description = "No tenés permiso para editar este álbum"),
            @ApiResponse(responseCode = "404", description = "Álbum no encontrado")
    })
    @PatchMapping("/artist/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<AlbumResponse> updateAlbumAsArtist(
            @Parameter(description = "ID del álbum") @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.updateAlbumAsArtist(id, request, credentials.getUser().getId()));
    }

    @Operation(summary = "Eliminar álbum como artista", description = "Elimina un álbum propio pendiente de aprobación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Álbum eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tenés permiso para eliminar este álbum"),
            @ApiResponse(responseCode = "404", description = "Álbum no encontrado")
    })
    @DeleteMapping("/artist/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Void> deleteAlbumAsArtist(
            @Parameter(description = "ID del álbum") @PathVariable Long id,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        albumService.deleteAlbumAsArtist(id, credentials.getUser().getId());
        return ResponseEntity.noContent().build();
    }

}
