package com.anthology.controller;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.requests.PlaylistUpdateRequest;
import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.requests.UserUpdateRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.dto.responses.UserResponse;
import com.anthology.service.PlaylistService;
import com.anthology.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@AllArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;
    @Operation(summary = "Crear playlist", description = "Crea un nuevo playlist base en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Playlist creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un playlist con ese nombre")
    })
    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(@Valid @RequestBody PlaylistRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body((playlistService.create(request)));
    }
    @Operation(summary = "Editar playlist", description = "Modifica parcialmente los datos de una playlist existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Playlist actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PlaylistResponse>updatePlaylist(@Parameter(description = "ID del playlist")@PathVariable Long id, @Valid @RequestBody PlaylistUpdateRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(playlistService.updatePlaylist(id,request));

    }
    @Operation(summary = "Eliminar playlist", description = "Realiza un borrado lógico del playlist ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Playlist eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(
            @Parameter(description = "ID del playlist") @PathVariable Long id){
        playlistService.deleatePlaylist(id);
        return ResponseEntity
                .noContent()
                .build();
    }
    @Operation(summary = "Listar playlist", description = "Devuelve todas los playlist del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de playlist obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> findAllPlaylist(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playlistService.findAllPlaylist());
    }
    @Operation(summary = "Detalle del playlist", description = "Devuelve el detalle de un playlist por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Playlist encontrado"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrado")
    })
    @GetMapping("{id}")
    public ResponseEntity<PlaylistResponse> findById(
            @Parameter(description = "ID del usuario") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playlistService.findById(id));
    }
}
