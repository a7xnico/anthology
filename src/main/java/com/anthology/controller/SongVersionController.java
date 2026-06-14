package com.anthology.controller;

import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import com.anthology.model.CredentialsEntity;
import com.anthology.service.SongVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs/{songId}/versions")
@AllArgsConstructor
@Tag(name = "Versiones de canciones", description = "Gestion y consulta de versiones de canciones")
public class SongVersionController {

    private final SongVersionService songVersionService;

    @Operation(summary = "Crear versión", description = "Sube un archivo MusicXML o Guitar Pro y genera el PDF de la versión")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Versión creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una versión para ese instrumento")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SongVersionResponse> createVersion(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "Instrumento de la versión") @RequestParam Instrument instrument,
            @Parameter(description = "Archivo MusicXML o Guitar Pro")
            @RequestPart("file") MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songVersionService.createVersionAsAdmin(songId, instrument, file));
    }

    @Operation(summary = "Subir versión como artista", description = "Sube un archivo MusicXML o Guitar Pro para una canción propia, queda pendiente de aprobación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Versión creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "403", description = "No tenés permiso para agregar versiones a esta canción"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una versión para ese instrumento")
    })
    @PostMapping(value = "/artist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<SongVersionResponse> createVersionAsArtist(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "Instrumento de la versión") @RequestParam Instrument instrument,
            @Parameter(description = "Archivo MusicXML o Guitar Pro") @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songVersionService.createVersionAsArtist(songId, instrument, file, credentials.getUser().getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<SongVersionResponse>> findVersionsBySongId(
            @Parameter(description = "ID de la canción") @PathVariable Long songId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findVersionsBySongId(songId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SongVersionResponse>> findAllVersionsBySongId(
            @PathVariable Long songId) {
        return ResponseEntity.ok(songVersionService.findAllVersionsBySongId(songId));
    }


    @Operation(summary = "Detalle de  versión", description = "Devuelve el detalle de una versión por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versión encontrada"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{versionId}")
    public ResponseEntity<SongVersionResponse> findVersionById(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findVersionById(songId, versionId));
    }

    @Operation(summary = "Mis versiones", description = "Devuelve todas las versiones subidas por el artista autenticado con su estado")
    @ApiResponse(responseCode = "200", description = "Lista de versiones obtenida exitosamente")
    @GetMapping("/api/artist/my-versions")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<SongVersionResponse>> findMyVersions(
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findMyVersions(credentials.getUser().getId()));
    }

    @Operation(summary = "Actualizar estado", description = "Aprueba o rechaza una versión pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @PatchMapping("/{versionId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongVersionResponse> updateStatus(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId,
            @Parameter(description = "Nuevo estado") @RequestParam Status status){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.updateStatus(songId, versionId, status));
    }

    @GetMapping("/api/versions/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SongVersionResponse>> findByStatus(
            @RequestParam Status status) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findByStatus(status));
    }


    @Operation(summary = "Eliminar versión", description = "Realiza un borrado lógico de la versión")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Versión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @DeleteMapping("/{versionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVersion(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId) {
        songVersionService.deleteVersion(songId, versionId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Eliminar versión como artista", description = "Realiza un borrado lógico de una versión propia pendiente de aprobación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Versión eliminada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No tenés permiso para eliminar esta versión"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @DeleteMapping("/artist/{versionId}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Void> deleteVersionAsArtist(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        songVersionService.deleteVersionAsArtist(songId, versionId, credentials.getUser().getId());
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Listar versiones eliminadas", description = "Devuelve todas las versiones eliminadas de una canción")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de versiones eliminadas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SongVersionResponse>> findDeletedVersionsBySongId(
            @PathVariable Long songId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findDeletedVersionsBySongId(songId));
    }






}
