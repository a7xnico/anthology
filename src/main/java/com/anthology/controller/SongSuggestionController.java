package com.anthology.controller;

import com.anthology.dto.requests.SongSuggestionRequest;
import com.anthology.dto.responses.SongSuggestionResponse;
import com.anthology.service.SongSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songsSugestions")
@AllArgsConstructor
public class SongSuggestionController {
    private final SongSuggestionService service;
    @Operation(summary = "Crear sugerencia de cancion", description = "Crea una nueva sugerencia de cancion base en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sugerencia creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una sugerencia con ese nombre")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SongSuggestionResponse> createSongSuggestion(@Valid @RequestBody SongSuggestionRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
    @Operation(summary = "Listar sugerencias", description = "Devuelve todas las sugerencias del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de sugerencias obtenida exitosamente")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SongSuggestionResponse>> findAllSuggestions(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findAllSongSuggestion());
    }
    @Operation(summary = "Detalle de la sugerencia", description = "Devuelve el detalle de una sugerencia por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sugerencia encontrada"),
            @ApiResponse(responseCode = "404", description = "sugerencia no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongSuggestionResponse> findById(
            @Parameter(description = "ID de la sugerencia") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }
    @Operation(summary = "rechazar sugerencia", description = "Realiza un borrado lógico de la sugerencia ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sugerencia eliminada y rechazada exitosamente"),
            @ApiResponse(responseCode = "404", description = "sugerencia no encontrada")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongSuggestionResponse> statusRejected(
            @Parameter(description = "ID de la sugerencia") @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.statusRejected(id));
    }
    @PatchMapping("/{id}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SongSuggestionResponse> finalizar(@PathVariable Long id) {
        SongSuggestionResponse songSuggestionResponse = service.statusAdded(id);

        return ResponseEntity.status(HttpStatus.OK).body(songSuggestionResponse);
    }
    @Operation(summary = "Eliminar sugerencia de cancion", description = "Realiza un borrado lógico de sugerencia de canción ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sugerencia de Canción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sugerencia de Canción no encontrada")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSongSuggestion(
            @Parameter(description = "ID de la sugerencia de canción") @PathVariable Long id){
        service.deleateSongSuggestion(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
