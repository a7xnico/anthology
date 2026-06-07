package com.anthology.controller;

import com.anthology.JwtAndSecurity.JwtService;
import com.anthology.dto.JwtDTO.AuthRequestDTO;
import com.anthology.dto.JwtDTO.AuthResponseDTO;
import com.anthology.dto.JwtDTO.RefreshTokenRequest;
import com.anthology.model.CredentialsEntity;
import com.anthology.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody
                                                        AuthRequestDTO authRequest){
        CredentialsEntity user =(CredentialsEntity) authService.authenticate(authRequest);
        System.out.println(user);
        String token = jwtService.generateToken(user);
        System.out.println(token);
        return ResponseEntity.ok(new AuthResponseDTO(token,
                user.getRefreshToken()));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody
                                                        RefreshTokenRequest request){
        return ResponseEntity.ok(authService.refreshAccessToken(request.refreshToken()));
    }


}
