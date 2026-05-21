package com.anthology.controller;

import com.anthology.dto.responses.SongResponse;
import com.anthology.enums.Instrument;
import com.anthology.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@AllArgsConstructor
public class SongController {
    private final SongService songService;

    @Operation(summary = "Buscar canciones", description = "Busca canciones aplicando filtros opcionales")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    @GetMapping
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
    @GetMapping()
    public ResponseEntity<List<SongResponse>> findByInstrument(
            @RequestParam Instrument instrument) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(songService.findByInstrument(instrument));
    }


}
