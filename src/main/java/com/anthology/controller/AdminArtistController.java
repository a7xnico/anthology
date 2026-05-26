package com.anthology.controller;

import com.anthology.dto.responses.ArtistResponse;
import com.anthology.model.ArtistRequest;
import com.anthology.service.ArtistService;
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
@RequestMapping("/api/admin/artists")
@AllArgsConstructor
@Tag(name = "Admin - Artistas", description = "Gestión de artistas por el administrador")
public class AdminArtistController {

    private final ArtistService artistService;


    @Operation(summary = "Crear Artista", description = "Crea un nuevo Artista en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artista Creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un Artista con ese Stage Name")
    })
    @PostMapping
    public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody ArtistRequest artistRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.createArtist(artistRequest));
    }


    @Operation(summary = "Modificar Artista", description = "Modifica los datos de un Artista")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ArtistResponse> updateArtist(@Parameter(description = "Id del Artista") @PathVariable Long id, @Valid @RequestBody ArtistRequest artistRequest){
        return ResponseEntity.status(HttpStatus.OK).body(artistService.updateById(id,artistRequest));
    }


    @Operation(summary = "Buscar Artistas", description = "Buscar todos los Artistas")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAllArtist(){
        return ResponseEntity.ok(artistService.findAllArtist());
    }




}
