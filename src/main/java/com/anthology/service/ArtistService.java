package com.anthology.service;


import com.anthology.dto.requests.ArtistRequest;
import com.anthology.dto.responses.ArtistResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.ArtistMapper;
import com.anthology.model.*;
import com.anthology.repository.ArtistRepository;
import com.anthology.repository.CredentialsRepository;
import com.anthology.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final  UserService userService;
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final CredentialsRepository credentialsRepository;
    private final RoleRepository roleRepository;


    @Transactional
    public ArtistResponse createArtist(ArtistRequest artistRequest){

        if(artistRepository.existsByStageName(artistRequest.stageName())){
            throw new DuplicateResourceException("Ya existe Artista con ese nombre");
        }

        User user = userService.findUserById(artistRequest.userId());

        Artist artist = artistMapper.toEntity(artistRequest);
        artist.setUser(user);

        artistRepository.save(artist);

        CredentialsEntity credentials = credentialsRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales no encontradas"));


        boolean yaEsArtista = credentials.getRoles().stream()
                .anyMatch(r -> r.getRole() == com.anthology.enums.Role.ARTIST);


        if (!yaEsArtista) {
            RoleEntity artistRole = roleRepository.findByRole(com.anthology.enums.Role.ARTIST)
                    .orElseThrow(() -> new ResourceNotFoundException("Rol ARTIST no encontrado en BD"));

            Set<RoleEntity> roles = credentials.getRoles();
            roles.add(artistRole);
            credentials.setRoles(roles);
            credentialsRepository.save(credentials);
            System.out.println("Rol ARTIST asignado al usuario " + user.getUsername());
        } else {
            System.out.println("El usuario " + user.getUsername() + " ya tenía el rol ARTIST.");
        }

        return artistMapper.toDTO(artist);
    }

    public Artist findArtistById(Long id){
        return artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artista no encontrado..."));
    }

    public ArtistResponse findById(Long id){
        return artistMapper.toDTO(findArtistById(id));
    }

    public ArtistResponse findByName(String stageName){
        return artistRepository.findByStageNameIgnoreCase(stageName)
                .map(artistMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Artista no encontrado"));
    }

    public List<ArtistResponse> findAllArtist(){
        return artistRepository.findAll().stream()
                .map(artistMapper::toDTO)
                .toList();
    }

    public ArtistResponse updateById( Long id, ArtistRequest artistRequest){
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artista No encontrado"));

        if(artistRequest.biography() != null) artist.setBiography(artistRequest.biography());
        if(artistRequest.instagram() != null) artist.setInstagram(artistRequest.instagram());
        if(artistRequest.spotify() != null) artist.setSpotify(artistRequest.spotify());
        if(artistRequest.youtube() != null) artist.setYoutube(artistRequest.youtube());

        return artistMapper.toDTO(artistRepository.save(artist));
    }

    @Transactional
    public void deleteArtist(Long id){
        Artist artist = findArtistById(id);


        credentialsRepository.findByUsername(artist.getUser().getUsername())
                .ifPresent(credentials -> {
                    credentials.getRoles().removeIf(
                            r -> r.getRole() == com.anthology.enums.Role.ARTIST
                    );
                    credentialsRepository.save(credentials);
                });

        artistRepository.delete(artist);
    }



}
