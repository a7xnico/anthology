package com.anthology.controller;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import com.anthology.model.CredentialsEntity;
import com.anthology.service.SongService;
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
@RequestMapping("/api/songs")
@AllArgsConstructor
@Tag(name = "canciones", description = "Gestión y consulta de canciones")
@PreAuthorize("denyAll")
public class SongController {
    private final SongService songService;


    @Operation(summary = "Crear canción", description = "Crea una nueva canción base en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Canción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una canción con ese título y artista")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody SongRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songService.createSong(request));
    }

    @Operation(summary = "Crear canción", description = "Crea una nueva canción base en el sistema como artista")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Canción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una canción con ese título y artista")
    })
    @PostMapping("/artist")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<SongResponse> createSongAsArtist(
            @Valid @RequestBody SongRequest request,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songService.createSongAsArtist(request, credentials.getUser().getId()));
    }

    @Operation(summary = "Editar canción", description = "Modifica parcialmente los datos de una canción existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongResponse> updateSong(
            @Parameter(description = "ID de la canción") @PathVariable Long id,
            @Valid @RequestBody SongUpdateRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.updateSong(id, request));
    }

    @Operation(summary = "Editar canción como artista", description = "El artista modifica parcialmente los datos de una canción mientras esta este pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @PatchMapping("/artist/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<SongResponse> updateSongAsArtist(
            @Parameter(description = "ID de la canción") @PathVariable Long id,
            @Valid @RequestBody SongUpdateRequest request,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.updateSongAsArtist(id, request, credentials.getUser().getId()));
    }

    @Operation(summary = "Eliminar canción", description = "Realiza un borrado lógico de la canción y sus versiones")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSong(
            @Parameter(description = "ID de la canción") @PathVariable Long id){
        songService.deleteSong(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Eliminar canción como artista", description = "Realiza un borrado lógico de la canción y sus versiones mientras esta este pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @DeleteMapping("/artist/{id}")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<Void> deleteSongAsArtist(
            @Parameter(description = "ID de la canción") @PathVariable Long id,
            @AuthenticationPrincipal CredentialsEntity credentials) {
        songService.deleteSongAsArtist(id, credentials.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todas las canciones", description = "Devuelve todas las canciones del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de canciones obtenida exitosamente")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SongResponse>> findAllSongs(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.findAllSongs());
    }

    @Operation(summary = "Buscar canciones", description = "Busca canciones aplicando filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongResponse>> findSongs(
            @Parameter(description = "Filtrar por título") @RequestParam(required = false) String title,
            @Parameter(description = "Filtrar por género") @RequestParam(required = false) String genre,
            @Parameter(description = "Filtrar por nombre de artista o banda") @RequestParam(required = false) String artistName,
            @Parameter(description = "Filtrar por álbum") @RequestParam(required = false) Long albumId) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.findSongs(title, genre, artistName, albumId));
    }

    @Operation(summary = "Detalle de canción", description = "Devuelve el detalle de una canción por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción encontrada"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SongResponse> findById(
            @Parameter(description = "ID de la canción") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.findById(id));
    }

    @Operation(summary = "Buscar por instrumento", description = "Devuelve canciones que tienen versión disponible para el instrumento indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Instrumento inválido")
    })
    @GetMapping("/by-instrument")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SongResponse>> findByInstrument(
            @RequestParam Instrument instrument) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.findByInstrument(instrument));
    }

    @Operation(summary = "Buscar mis canciones", description = "Devuelve canciones creadas por el artista")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    @GetMapping("/my-songs")
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<List<SongResponse>> findMySongs(
            @AuthenticationPrincipal CredentialsEntity credentials) {
        return ResponseEntity.ok(songService.findMySongs(credentials.getUser().getId()));
    }

    @Operation(summary = "Listar canciones por estado", description = "Devuelve canciones filtradas por su estado")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SongResponse>> findByStatus(
            @Parameter(description = "Estado de la canción") @RequestParam Status status){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.findByStatus(status));
    }

    @Operation(summary = "Actualizar estado de una canción", description = "Aprueba o rechaza una canción pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongResponse> updateStatus(
            @Parameter(description = "ID de la canción") @PathVariable Long id,
            @Parameter(description = "Nuevo Estado") @RequestParam Status status){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.updateStatus(id, status));
    }


}
