package com.anthology.service;


import com.anthology.dto.requests.ArtistRequest;
import com.anthology.dto.responses.ArtistResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.ArtistMapper;
import com.anthology.model.Artist;
import com.anthology.model.ArtistSuggestion;
import com.anthology.model.User;
import com.anthology.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final  UserService userService;
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistResponse createArtist(ArtistRequest artistRequest){
        if(artistRepository.existsByStageName(artistRequest.stageName())){
            throw new DuplicateResourceException("Ya existe Artista con ese nombre");
        }

        User user = userService.findUserById(artistRequest.userId());

        Artist artist = artistMapper.toEntity(artistRequest);
        artist.setUser(user);
        artist.setCreatedAt(LocalDateTime.now());

        return artistMapper.toDTO(artistRepository.save(artist));
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

    public Artist findByUserId(Long userId){
        return artistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de artista no encontrado"));}

    public void deleteArtist(Long id){
        Artist artist = findArtistById(id);
        artistRepository.delete(artist);
    }
}

