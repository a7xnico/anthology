package com.anthology.service;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.SongMapper;
import com.anthology.model.Album;
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
    private final AlbumService albumService;

    public SongResponse createSong(SongRequest request){
        if (songRepository.existsByTitleIgnoreCaseAndArtistNameIgnoreCase(request.title(), request.artistName()))
            throw new DuplicateResourceException("Ya existe una cancion con ese titulo y artista");

        Song song = songMapper.toEntity(request);

        if (request.albumId() != null) {
            Album album = albumService.findAlbumById(request.albumId());
            song.setAlbum(album);
        }

        return songMapper.toDTO(songRepository.save(song));
    }

    public SongResponse updateSong(Long id, SongUpdateRequest request){
        Song song = findSongById(id);

        if (request.title() != null) song.setTitle(request.title());
        if (request.artistName() != null) song.setArtistName(request.artistName());
        if (request.genre() != null) song.setGenre(request.genre());

        if (request.albumId() != null) song.setAlbum(albumService.findAlbumById(request.albumId()));
        else song.setAlbum(null);

        return songMapper.toDTO(songRepository.save(song));
    }

    public void deleteSong(Long id){
        Song song = findSongById(id);
        song.softDelete();
        songRepository.save(song);
    }

    public List<SongResponse> findSongs(String title, String genre, String artistName, Long albumId){
        return songRepository.findByFilters(title, genre, artistName, albumId)
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

    public List<SongResponse> findByInstrument(Instrument instrument) {
        return songRepository.findByInstrument(instrument)
                .stream()
                .map(songMapper::toDTO)
                .toList();
    }

    public List<SongResponse> findByStatus(Status status){
        return songRepository.findByStatus(status)
                .stream()
                .map(songMapper::toDTO)
                .toList();
    }

    public SongResponse updateStatus(Long id, Status status){
        Song song = findSongById(id);
        song.setStatus(status);
        return songMapper.toDTO(songRepository.save(song));
    }

    public Song findSongById(Long id){
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancion no encontrada"));
    }


}
