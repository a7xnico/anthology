package com.anthology.controller;


import com.anthology.dto.requests.ArtistRequest;
import com.anthology.dto.requests.ArtistUpdateRequest;
import com.anthology.dto.responses.ArtistResponse;
import com.anthology.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@AllArgsConstructor
@Tag(name = "Admin - Artistas", description = "Gestion y consulta de Artistas")
public class ArtistController {
    private final ArtistService artistService;


    ///  privados


    @Operation(summary = "Crear Artista", description = "Crea un nuevo Artista en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artista Creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un Artista con ese Stage Name")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody ArtistRequest artistRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.createArtist(artistRequest));
    }


    @Operation(summary = "Modificar Artista", description = "Modifica los datos de un Artista")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtistResponse> updateArtist(@Parameter(description = "Id del Artista") @PathVariable Long id, @Valid @RequestBody ArtistUpdateRequest artistUpdateRequest){
        return ResponseEntity.status(HttpStatus.OK).body(artistService.updateById(id, artistUpdateRequest));
    }




    /// publicos
    @Operation(summary = "Buscar Artistas", description = "Buscar todos los Artistas")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAllArtist(){
        return ResponseEntity.ok(artistService.findAllArtist());
    }

    @Operation(summary = "Buscar Artista especifico", description = "Devuelve datos de un Artista especifico buscado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista encontrado"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> findById(@Parameter(description = "ID del Artista") @PathVariable Long id){
        return ResponseEntity.ok(artistService.findById(id));
    }

    @Operation(summary = "Buscar Artista por nombre", description = "Devuelve datos de un Artista especifico buscado por nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista encontrado"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buscar")
    public ResponseEntity<ArtistResponse> findByArtistStageName(@Parameter(description = "Stage name del Artista")@RequestParam @NotBlank String stageName){
        return ResponseEntity.ok(artistService.findByName(stageName));
    }

    @Operation(summary = "Eliminar Artista", description = "Realiza un borrado lógico del Artista ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Artista eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteArtist(
            @Parameter(description = "ID del artista") @PathVariable Long id){
        artistService.deleteArtist(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
