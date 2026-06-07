package com.anthology.service;

import com.anthology.dto.requests.SongSuggestionRequest;
import com.anthology.dto.responses.SongSuggestionResponse;
import com.anthology.enums.SongSuggestionStatus;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.SongSuggestionMapper;
import com.anthology.model.SongSuggestion;
import com.anthology.model.User;
import com.anthology.repository.SongSuggestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SongSuggestionService {
    private final SongSuggestionRepository repository;
    private final SongSuggestionMapper mapper;
    private final UserService userService;

    public SongSuggestionResponse create(SongSuggestionRequest request)
    {


        if(repository.exitsByIdUserAndArtist(request.title(), request.artistName()))
            throw new DuplicateResourceException("ya existe una sugerencia para esa cancion de ese artista");
        User user=userService.findUserById(request.idUser());
        SongSuggestion songSuggestion=mapper.toEntity(request);
        songSuggestion.setUser(user);
        return mapper.toDTO(repository.save(songSuggestion));

    }
    public SongSuggestion findSongSuggestionByid(Long id)
    {
        return repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("no se encontro la cancion sugerida"));

    }
    public List<SongSuggestionResponse> findAllSongSuggestion()
    {
        return repository
                .findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

    }
    public SongSuggestionResponse findById(Long id)
    {
        return mapper.toDTO(findSongSuggestionByid(id));
    }
    public void deleateSongSuggestion(Long id)
    {
        SongSuggestion songSuggestion=findSongSuggestionByid(id);
        repository.delete(songSuggestion);
    }
    public SongSuggestionResponse statusAdded(Long id)
    {
       SongSuggestion songSuggestion=repository.findByIdAndStatus(id,SongSuggestionStatus.ADDED).orElseThrow(()-> new ResourceNotFoundException("no se encontro el id"));
       songSuggestion.setStatus(SongSuggestionStatus.ADDED);
       return mapper.toDTO(songSuggestion);
    }
    public void statusRejected(Long id)
    {
        SongSuggestion songSuggestion=repository.findByIdAndStatus(id,SongSuggestionStatus.REJECTED).orElseThrow(()-> new ResourceNotFoundException("no se encontro el id"));
        songSuggestion.setStatus(SongSuggestionStatus.REJECTED);
        deleateSongSuggestion(id);
    }

}
