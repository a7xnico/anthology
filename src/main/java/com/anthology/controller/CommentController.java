package com.anthology.controller;

import com.anthology.dto.requests.CommentsRequest;
import com.anthology.dto.responses.CommentsResponse;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.service.CommentService;
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
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Crear comentario", description = "Crea un nuevo comentario  en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<CommentsResponse> createComment(@Valid @RequestBody CommentsRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request));
    }
    @Operation(summary = "Eliminar comentario", description = "Realiza un borrado lógico del comentario ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comentario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID del comentario") @PathVariable Long id){
        commentService.deleateComment(id);
        return ResponseEntity
                .noContent()
                .build();
    }
    @Operation(summary = "Listar comentarios", description = "Devuelve todas los comentarios del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de comentarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<CommentsResponse>> findAllComments(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findAll());
    }
    @Operation(summary = "Detalle del comentario", description = "Devuelve el detalle de un comentario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comentario encontrado"),
            @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
    })
    @GetMapping("{id}")
    public ResponseEntity<CommentsResponse> findById(
            @Parameter(description = "ID del comentario") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findByid(id));
    }

}
