package com.anthology.service;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.mapper.SongMapper;
import com.anthology.model.Song;
import com.anthology.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final SongMapper songMapper;

    public SongResponse createSong(SongRequest request){
        if (songRepository.existsByTitleIgnoreCaseAndArtistNameIgnoreCase(request.title(), request.artistName()))
            throw new DuplicateResourceException("Ya existe una cancion con ese titulo y artista");

        Song song = songMapper.toEntity(request);
        return songMapper.toDTO(songRepository.save(song));
    }
}
