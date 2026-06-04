package com.anthology.controller;

import com.anthology.dto.requests.ArtistSuggestionRequest;
import com.anthology.dto.responses.ArtistSuggestionResponse;
import com.anthology.service.ArtistSuggestionService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistSuggestions")
@AllArgsConstructor
@Tag(name = "Sugerencia Artista", description = "Consulta y creacion sugerencias artistas")
public class ArtistSuggestionController {

    private final ArtistSuggestionService artistSuggestionService;

    @Operation(summary = "Crear sugerencia", description = "Crea una nueva sugerencia de artista")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sugerencia creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtistSuggestionResponse> createArtistSuggestion(@Valid @RequestBody ArtistSuggestionRequest artistSuggestionRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(artistSuggestionService.createArtistSuggestion(artistSuggestionRequest));
    }

    @Operation(summary = "Buscar sugerencias artistas", description = "Busca a todas las sugerencias artistas")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    @GetMapping
    public ResponseEntity<List<ArtistSuggestionResponse>> findAllArtistsSuggestion(){
        return ResponseEntity.ok(artistSuggestionService.findAll());
    }
    @Operation(summary = "Modifica Artistas", description = "Bucas a Artista por Id y modifica sus campos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sugerencia Artista actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Sugerencia Artista no encontrada")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtistSuggestionResponse> updateArtistSuggestion(@Parameter(description = "ID Artista") @PathVariable Long id, @Valid @RequestBody ArtistSuggestionRequest artistSuggestionRequest){
        return ResponseEntity.ok(artistSuggestionService.cambiarEstadoSuggestion(id, artistSuggestionRequest));
    }

}
