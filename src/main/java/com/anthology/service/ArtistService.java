package com.anthology.service;


import com.anthology.dto.requests.ArtistRequest;
import com.anthology.dto.requests.ArtistUpdateRequest;
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
import java.util.Optional;
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

    public ArtistResponse updateById(Long id, ArtistUpdateRequest artistUpdateRequest){
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artista No encontrado"));

        if(artistUpdateRequest.biography() != null) artist.setBiography(artistUpdateRequest.biography());
        if(artistUpdateRequest.instagram() != null) artist.setInstagram(artistUpdateRequest.instagram());
        if(artistUpdateRequest.spotify() != null) artist.setSpotify(artistUpdateRequest.spotify());
        if(artistUpdateRequest.youtube() != null) artist.setYoutube(artistUpdateRequest.youtube());

        return artistMapper.toDTO(artistRepository.save(artist));
    }
    public ArtistResponse updateMyArtist(Long userID, ArtistUpdateRequest request){
        Artist artist = artistRepository.findByUserId(userID)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de artista no encontrado"));
        if (request.biography() != null) artist.setBiography(request.biography());
        if (request.instagram() != null) artist.setInstagram(request.instagram());
        if (request.spotify() != null) artist.setSpotify(request.spotify());
        if (request.youtube() != null) artist.setYoutube(request.youtube());

        return artistMapper.toDTO(artistRepository.save(artist));
    }

    public Artist findByUserId(Long userId){
        return artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de artista no encontrado"));
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

    public Optional<Artist> findByStageNameOptional(String stageName) {
        return artistRepository.findByStageNameIgnoreCase(stageName);
    }

}
