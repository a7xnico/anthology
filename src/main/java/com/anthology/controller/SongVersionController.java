package com.anthology.controller;

import com.anthology.dto.requests.SongVersionRequest;
import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.enums.Status;
import com.anthology.service.SongVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SongVersionResponse> createVersion(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @RequestPart("data") @Valid SongVersionRequest request,
            @Parameter(description = "Archivo MusicXML o Guitar Pro")
            @RequestPart("file") MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songVersionService.createVersion(songId, request, file));
    }

    @GetMapping
    public ResponseEntity<  List<SongVersionResponse>> findVersionsBySongId(
            @Parameter(description = "ID de la canción") @PathVariable Long songId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findVersionsBySongId(songId));
    }


    @Operation(summary = "Detalle de  versión", description = "Devuelve el detalle de una versión por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versión encontrada"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @GetMapping("/{versionId}")
    public ResponseEntity<SongVersionResponse> findVersionById(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.findVersionById(songId, versionId));
    }

    @Operation(summary = "Actualizar estado", description = "Aprueba o rechaza una versión pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @PatchMapping("/{versionId}/status")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongVersionResponse> updateStatus(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId,
            @Parameter(description = "Nuevo estado") @RequestParam Status status){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songVersionService.updateStatus(songId, versionId, status));
    }


    @Operation(summary = "Eliminar versión", description = "Realiza un borrado lógico de la versión")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Versión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Versión no encontrada")
    })
    @DeleteMapping("/{versionId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVersion(
            @Parameter(description = "ID de la canción") @PathVariable Long songId,
            @Parameter(description = "ID de la versión") @PathVariable Long versionId) {
        songVersionService.deleteVersion(songId, versionId);
        return ResponseEntity
                .noContent()
                .build();
    }








}
