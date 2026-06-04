package com.anthology.controller;

import com.anthology.JwtAndSecurity.JwtService;
import com.anthology.dto.JwtDTO.AuthRequestDTO;
import com.anthology.dto.JwtDTO.AuthResponseDTO;
import com.anthology.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody AuthRequestDTO authRequestDTO){
        UserDetails userDetails = authService.authenticate(authRequestDTO);
        String token = jwtService.generateToken(userDetails);
        return  ResponseEntity.ok(new AuthResponseDTO("Bearer ",token));
    }
}
