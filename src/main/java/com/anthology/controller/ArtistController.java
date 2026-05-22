package com.anthology.controller;


import com.anthology.dto.responses.AlbumResponse;
import com.anthology.dto.responses.ArtistResponse;
import com.anthology.model.ArtistRequest;
import com.anthology.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@AllArgsConstructor

@Tag(name = "Artistas", description = "Consulta de Artistas")
public class ArtistController {
    private final ArtistService artistService;


    @Operation(summary = "Buscar Artistas", description = "Buscar todos los Artistas")
    @ApiResponse(responseCode = "200", description = "Busqueda realizada correctamente")
    @GetMapping
    public ResponseEntity<List<ArtistResponse>> findAllArtist(){
        return ResponseEntity.ok(artistService.findAllArtist());
    }

    @Operation(summary = "Buscar Artista especifico", description = "Devuelve datos de un Artista especifico buscado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Artista encontrado"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(artistService.findById(id));
    }



    @Operation(summary = "Crear Artista", description = "Crea un nuevo Artista en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Artista Creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un Artista con ese Stage Name")
    })
    @PostMapping
    public ResponseEntity<ArtistResponse> create(@Valid @RequestBody ArtistRequest artistRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.createArtist(artistRequest));
    }










}
