package com.anthology.service;

import com.anthology.dto.requests.ArtistSuggestionRequest;
import com.anthology.dto.responses.ArtistSuggestionResponse;
import com.anthology.enums.Status;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.exception.SystemRuleException;
import com.anthology.mapper.ArtistSuggestionMapper;
import com.anthology.model.Artist;
import com.anthology.model.ArtistSuggestion;
import com.anthology.model.User;
import com.anthology.repository.ArtistSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistSuggestionService {

    private final ArtistSuggestionRepository artistSuggestionRepository;
    private final ArtistSuggestionMapper artistSuggestionMapper;
    private final UserService userService;

    public ArtistSuggestionResponse createArtistSuggestion(ArtistSuggestionRequest artistSuggestionRequest){

        User user = userService.findUserById(artistSuggestionRequest.userId());

        ArtistSuggestion artistSuggestion = artistSuggestionMapper.toEntity(artistSuggestionRequest);

        artistSuggestion.setUser(user);

        return artistSuggestionMapper.toDTO(artistSuggestionRepository.save(artistSuggestion));
    }


    public List<ArtistSuggestionResponse> findAll(){
        return artistSuggestionRepository.findAll().stream()
                .map(artistSuggestionMapper::toDTO)
                .toList();
    }


    public ArtistSuggestion findArtistSuggestionById(Long id){
        return artistSuggestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sugerencia Artista no encontrada"));
    }

    public ArtistSuggestionResponse findById(Long id){
        return artistSuggestionMapper.toDTO(findArtistSuggestionById(id));
    }

    public ArtistSuggestionResponse cambiarEstadoSuggestion(Long id, ArtistSuggestionRequest artistSuggestionRequest){
        ArtistSuggestion artistSuggestion = artistSuggestionRepository.findById(artistSuggestionRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtistSuggestion no encontrada..."));

        if (artistSuggestion.getStatus() == Status.REJECTED){
            throw new SystemRuleException("Sugerencia Artista ya fue rechazada...");
        }
        if (artistSuggestion.getStatus() == Status.PENDING){
            artistSuggestion.setStatus(Status.APPROVED);
        }

       return artistSuggestionMapper.toDTO( artistSuggestionRepository.save(artistSuggestion));
    }


    public void deleteArtistSuggestion(Long id){
        ArtistSuggestion artistSuggestion = findArtistSuggestionById(id);
        artistSuggestionRepository.delete(artistSuggestion);
    }



}
