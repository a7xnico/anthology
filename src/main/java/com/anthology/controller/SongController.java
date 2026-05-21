package com.anthology.controller;

import com.anthology.dto.responses.SongResponse;
import com.anthology.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
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
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String artistName){
        return ResponseEntity.status(HttpStatus.OK).body(songService.findSongs(title, genre, artistName));
    }

    @Operation(summary = "Detalle de canción", description = "Devuelve el detalle de una canción por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción encontrada"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(songService.findById(id));
    }


}
