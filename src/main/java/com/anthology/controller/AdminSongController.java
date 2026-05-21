package com.anthology.controller;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/songs")
@AllArgsConstructor
@Tag(name = "Admin - Canciones", description = "Gestión de canciones por el administrador")
public class AdminSongController {
    private final SongService songService;

    @Operation(summary = "Crear canción", description = "Crea una nueva canción base en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Canción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una canción con ese título y artista")
    })
    @PostMapping
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody SongRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.createSong(request));
    }

    @Operation(summary = "Editar canción", description = "Modifica parcialmente los datos de una canción existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<SongResponse> updateSong(@PathVariable Long id,
                                                   @Valid @RequestBody SongUpdateRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(songService.updateSong(id, request));
    }

    @Operation(summary = "Eliminar canción", description = "Realiza un borrado lógico de la canción y sus versiones")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id){
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar canciones", description = "Devuelve todas las canciones del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de canciones obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<SongResponse>> findAllSongs(){
        return ResponseEntity.status(HttpStatus.OK).body(songService.findAllSongs());
    }


}
