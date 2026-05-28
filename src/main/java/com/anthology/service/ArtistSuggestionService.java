package com.anthology.service;

import com.anthology.dto.requests.ArtistSuggestionRequest;
import com.anthology.dto.responses.ArtistSuggestionResponse;
import com.anthology.enums.Status;
import com.anthology.mapper.ArtistSuggestionMapper;
import com.anthology.model.Artist;
import com.anthology.model.ArtistSuggestion;
import com.anthology.repository.ArtistSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistSuggestionService {

    private final ArtistSuggestionRepository artistSuggestionRepository;
    private final ArtistSuggestionMapper artistSuggestionMapper;
  ///  private final UserService userService;

    public ArtistSuggestionResponse createArtistSuggestion(ArtistSuggestionRequest artistSuggestionRequest){
        ///  falta maneja user
       // if(userService.findById(artistRequest.userId()))
      //  .orElseThrow

        ArtistSuggestion artistSuggestion = artistSuggestionMapper.toEntity(artistSuggestionRequest);


        artistSuggestion.setStatus(Status.APPROVED);

        return artistSuggestionMapper.toDTO(artistSuggestionRepository.save(artistSuggestion));
    }


    public List<ArtistSuggestionResponse> findAll(){
        return artistSuggestionRepository.findAll().stream()
                .map(artistSuggestionMapper::toDTO)
                .toList();
    }



}
