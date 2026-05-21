package com.anthology.controller;

import com.anthology.dto.responses.AlbumResponse;
import com.anthology.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
@AllArgsConstructor
@Tag(name = "Álbumes", description = "Consulta de álbumes")
public class AlbumController {
    private final AlbumService albumService;

    @Operation(summary = "Listar álbumes", description = "Devuelve todos los álbumes disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de álbumes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<AlbumResponse>> findAllAlbums() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.findAllAlbums());
    }

    @Operation(summary = "Detalle de álbum", description = "Devuelve el detalle de un álbum por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "álbum encontrado"),
            @ApiResponse(responseCode = "404", description = "álbum no encontrado")
    })
    @GetMapping("{id}")
    public ResponseEntity<AlbumResponse> findById(
            @Parameter(description = "ID del álbum") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(albumService.findById(id));
    }





}
