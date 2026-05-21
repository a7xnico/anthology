package com.anthology.controller;


import com.anthology.dto.requests.SongVersionRequest;
import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.service.SongVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/admin/songs/{songId}/versions")
@AllArgsConstructor
public class AdminSongVersionController {
    private final SongVersionService songVersionService;

    @Operation(summary = "Crear versión", description = "Sube un archivo MusicXML o Guitar Pro y genera el PDF de la versión")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Versión creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una versión para ese instrumento")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SongVersionResponse> createVersion(
            @PathVariable Long songId,
            @RequestPart("data") @Valid SongVersionRequest request,
            @RequestPart("file")MultipartFile file){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(songVersionService.createVersion(songId, request, file));
    }
}
