package com.anthology.service;


import com.anthology.JwtAndSecurity.JwtService;
import com.anthology.dto.JwtDTO.AuthRequestDTO;
import com.anthology.dto.JwtDTO.AuthResponseDTO;
import com.anthology.model.CredentialsEntity;
import com.anthology.repository.CredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserDetails authenticate(AuthRequestDTO input){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.username(),
                input.password()
        ));
        CredentialsEntity user =  credentialsRepository
                .findByUsername(input.username())
                .orElseThrow();

        String refreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        credentialsRepository.save(user);

        return user;


    }

    @Transactional
    public AuthResponseDTO refreshAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);

        CredentialsEntity user =
                credentialsRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token does not match");
        }

        if (!jwtService.validateRefreshToken(refreshToken, user)) {
            throw new IllegalArgumentException("Refresh token expired or invalid");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        user.setRefreshToken(newRefreshToken);
        credentialsRepository.save(user);

        return new AuthResponseDTO(newAccessToken, newRefreshToken);
    }



}
