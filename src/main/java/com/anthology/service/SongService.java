package com.anthology.service;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.SongMapper;
import com.anthology.model.Song;
import com.anthology.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public SongResponse updateSong(Long id, SongUpdateRequest request){
        Song song = findSongById(id);

        songMapper.updateEntity(request, song);
        return songMapper.toDTO(songRepository.save(song));
    }

    public void deleteSong(Long id){
        Song song = findSongById(id);

        song.softDelete();
        songRepository.save(song);
    }

    public List<SongResponse> findSongs(String title, String genre, String artistName){
        return songRepository.findByFilters(title, genre, artistName)
                .stream()
                .map(songMapper::toDTO)
                .toList();
    }

    public List<SongResponse> findAllSongs(){
        return songRepository.findAll()
                .stream()
                .map(songMapper::toDTO)
                .toList();
    }

    public SongResponse findById(Long id){
        return songMapper.toDTO(findSongById(id));
    }

    public Song findSongById(Long id){
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancion no encontrada"));
    }


}
