package com.anthology.service;


import com.anthology.dto.JwtDTO.AuthRequestDTO;
import com.anthology.repository.CredentialsRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CredentialsRepository credentialsRepository;
    private final AuthenticationManager authenticationManager;

    public AuthService(CredentialsRepository credentialsRepository, AuthenticationManager authenticationManager){
        this.credentialsRepository = credentialsRepository;
        this.authenticationManager = authenticationManager;
    }

    public UserDetails authenticate(AuthRequestDTO input){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.username(),
                input.password()
        ));
        return credentialsRepository.findByUsername(input.username()).orElseThrow();
    }
}
