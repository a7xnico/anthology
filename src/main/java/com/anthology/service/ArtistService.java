package com.anthology.service;


import com.anthology.dto.responses.ArtistResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.ArtistMapper;
import com.anthology.model.Artist;
import com.anthology.model.ArtistRequest;
import com.anthology.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private  final ArtistService artistService;
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;


    public ArtistResponse createArtist(ArtistRequest artistRequest){
        if(artistRepository.existsByStageName(artistRequest.getStageName())){
            throw new DuplicateResourceException("Ya existe Artista con ese nombre");
        }
        Artist artist = artistMapper.toEntity(artistRequest);

        artist.setCreatedAt(LocalDateTime.now());
        /// falta manejar user
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


}
