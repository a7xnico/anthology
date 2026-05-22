package com.anthology.controller;

import com.anthology.dto.requests.AlbumRequest;
import com.anthology.dto.requests.AlbumUpdateRequest;
import com.anthology.dto.responses.AlbumResponse;
import com.anthology.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/admin/albums")
@AllArgsConstructor
@Tag(name = "Admin - Álbumes", description = "Gestión de álbumes por el administrador")
public class AdminAlbumController {
    private final AlbumService albumService;

    @Operation(summary = "Crear álbum", description = "Crea un nuevo álbum en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Álbum creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un álbum con ese título y artista")
    })
    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody AlbumRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(albumService.createAlbum(request));
    }

    @Operation(summary = "Editar álbum", description = "Modifica parcialmente los datos de un álbum existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Álbum actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Álbum no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @Parameter(description = "ID del álbum") @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.updateAlbum(id, request));
    }

    @Operation(summary = "Eliminar álbum", description = "Elimina un álbum del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Álbum eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Álbum no encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAlbum(
            @Parameter(description = "ID del álbum") @PathVariable Long id){
        albumService.deleteAlbum(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "Listar álbumes", description = "Devuelve todos los álbumes del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de álbumes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<AlbumResponse>> findAllAlbums(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.findAllAlbums());
    }

}
