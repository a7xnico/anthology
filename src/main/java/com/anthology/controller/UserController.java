package com.anthology.controller;

import com.anthology.dto.requests.UserRequest;
import com.anthology.dto.requests.UserUpdateRequest;
import com.anthology.dto.responses.AlbumResponse;
import com.anthology.dto.responses.SongResponse;
import com.anthology.dto.responses.UserResponse;
import com.anthology.service.UserService;
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
@RequestMapping("/api/users")
@AllArgsConstructor

public class UserController {
    private final UserService userService;
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario base en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese nombre")
    })
    @PostMapping
public ResponseEntity<UserResponse>createUser(@Valid @RequestBody UserRequest request)
{
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
}
    @Operation(summary = "Editar usuario", description = "Modifica parcialmente los datos de un usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PatchMapping("/{id}")
public ResponseEntity<UserResponse>updateUser(@Parameter(description = "ID del usuario")@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, request));

    }
    @Operation(summary = "Eliminar ususario", description = "Realiza un borrado lógico del usuario ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario") @PathVariable Long id){
        userService.deleateUser(id);
        return ResponseEntity
                .noContent()
                .build();
    }
    @Operation(summary = "Listar usuarios", description = "Devuelve todas los usuarios del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAllUsers());
    }
    @Operation(summary = "Detalle de usuario", description = "Devuelve el detalle de un usuario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "usuario no encontrado")
    })
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> findById(
            @Parameter(description = "ID del usuario") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(id));
    }

}
