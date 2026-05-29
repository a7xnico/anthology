package com.anthology.service;


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

    private  final ArtistService artistService;
    private final  UserService userService;
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistResponse createArtist(ArtistSuggestion artistRequest){
        if(artistRepository.existsByStageName(artistRequest.getStageName())){
            throw new DuplicateResourceException("Ya existe Artista con ese nombre");
        }
        ///  User manejado en teoria falta probar
        User user = userService.findUserById(artistRequest.getUser().getId());


        Artist artist = artistMapper.toEntity(artistRequest);
        artist.setCreatedAt(LocalDateTime.now());

        return artistMapper.toDTO(artistRepository.save(artist));
    }

    public ArtistResponse findById(Long id){
        return artistRepository.findById(id)
                .map(artistMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Artista no encontrado..."));
    }

    public List<ArtistResponse> findAllArtist(){
        return artistRepository.findAll().stream()
                .map(artistMapper::toDTO)
                .toList();
    }

    public ArtistResponse updateById( Long id, ArtistSuggestion artistRequest){
        Artist artist = artistRepository.findArtistById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artista No encontrado"));

        if(artistRequest.getBiography() != null) artist.setBiography(artist.getBiography());
        if(artistRequest.getBiography() != null)  artist.setBiography(artistRequest.getBiography());
        if(artistRequest.getInstagram() != null) artist.setInstagram(artistRequest.getInstagram());
        if(artistRequest.getSpotify() != null) artist.setSpotify(artistRequest.getSpotify());
        if(artistRequest.getYoutube() != null) artist.setYoutube(artistRequest.getYoutube());

        return artistMapper.toDTO(artistRepository.save(artist));
    }

}
