package com.anthology.controller;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.requests.PlaylistUpdateRequest;
import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.requests.UserUpdateRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.dto.responses.UserResponse;
import com.anthology.model.CredentialsEntity;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistResponse> createPlaylist(@AuthenticationPrincipal CredentialsEntity credentials,
                                                           @Valid @RequestBody PlaylistRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body((playlistService.create(request, credentials)));
    }
    @Operation(summary = "Editar playlist", description = "Modifica parcialmente los datos de una playlist existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Playlist actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrado")
    })
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistResponse> updatePlaylist(
            @AuthenticationPrincipal CredentialsEntity credentials,
            @PathVariable Long id, @Valid @RequestBody PlaylistUpdateRequest request) {
        return ResponseEntity.ok(playlistService.updatePlaylist(id, request, credentials));
    }


    @Operation(summary = "Eliminar playlist", description = "Realiza un borrado lógico del playlist ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Playlist eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePlaylist(
            @AuthenticationPrincipal CredentialsEntity credentials, @PathVariable Long id) {
        playlistService.deletePlaylist(id, credentials);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar playlist", description = "Devuelve todas los playlist del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de playlist obtenida exitosamente")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> findById(
            @AuthenticationPrincipal CredentialsEntity credentials, @PathVariable Long id) {
        return ResponseEntity.ok(playlistService.findById(id, credentials));
    }

    @Operation(summary = "Playlists de usuario", description = "Devuelve todos sus playlists por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })

   @GetMapping("{id}/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PlaylistResponse>> findByuser(
            @AuthenticationPrincipal CredentialsEntity credentials, @PathVariable Long id) {
        return ResponseEntity.ok(playlistService.findByUser(id, credentials));
    }

    @Operation(summary = "agregar version de cancion al playlist", description = "Agrega una version de una cancion en una playlist existente por sus ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancion agregada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrado"),
            @ApiResponse(responseCode = "404", description = "SongVersion no enconntrada no encontrado")
    })

   @PatchMapping("/{idPlaylist}/canciones/{idSongVersion}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistResponse> agregarCancion(
            @AuthenticationPrincipal CredentialsEntity credentials,
            @PathVariable Long idPlaylist, @PathVariable Long idSongVersion) {
        return ResponseEntity.ok(playlistService.agregarCancion(idPlaylist, idSongVersion, credentials));
    }

   @DeleteMapping("/{idPlaylist}/canciones/{idSongVersion}")
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<PlaylistResponse> eliminarCancion(
           @AuthenticationPrincipal CredentialsEntity credentials,
           @PathVariable Long idPlaylist, @PathVariable Long idSongVersion) {
       return ResponseEntity.ok(playlistService.eliminarCancion(idPlaylist, idSongVersion, credentials));
   }
}
