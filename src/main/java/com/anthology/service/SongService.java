package com.anthology.service;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.enums.Instrument;
import com.anthology.enums.NotificationType;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.exception.UnauthorizedException;
import com.anthology.mapper.SongMapper;
import com.anthology.model.Album;
import com.anthology.model.Artist;
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
    private final ArtistService artistService;
    private final NotificationService notificationService;

    public SongResponse createSong(SongRequest request){
        if (songRepository.existsByTitleIgnoreCaseAndArtistNameIgnoreCase(request.title(), request.artistName()))
            throw new DuplicateResourceException("Ya existe una cancion con ese titulo y artista");

        Song song = songMapper.toEntity(request);

        if (request.albumId() != null)
            song.setAlbum(albumService.findAlbumById(request.albumId()));

        return songMapper.toDTO(songRepository.save(song));
    }

    public SongResponse createSongAsArtist(SongRequest request, Long userId){
        Artist artist = artistService.findByUserId(userId);

        if (songRepository.existsByTitleIgnoreCaseAndArtistNameIgnoreCase(request.title(), request.artistName()))
            throw new DuplicateResourceException("Ya existe una cancion con ese titulo y artista");

        Song song = songMapper.toEntity(request);
        song.setStatus(Status.PENDING);
        song.setArtist(artist);

        if (request.albumId() != null) {
            Album album = albumService.findAlbumById(request.albumId());

            if (album.getArtist() == null || !album.getArtist().getId().equals(artist.getId()))
                throw new UnauthorizedException("No tenés permiso para usar ese álbum");

            song.setAlbum(album);
        }

        Song saved = songRepository.save(song);

        notificationService.create(
                "El artista " + artist.getStageName() + " subió una canción pendiente: " + song.getTitle(),
                NotificationType.SONG_PENDING
        );

        return songMapper.toDTO(saved);
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

    public SongResponse updateSongAsArtist(Long songId, SongUpdateRequest request, Long userId){
        Artist artist = artistService.findByUserId(userId);
        Song song = findSongById(songId);

        if (song.getArtist() == null || !song.getArtist().getId().equals(artist.getId()))
            throw new UnauthorizedException("No tenés permiso para editar esta canción");

        if (song.getStatus() == Status.APPROVED)
            throw new UnauthorizedException("No podés editar una canción ya aprobada");

        return updateSong(songId, request);
    }

    public void deleteSong(Long id){
        Song song = findSongById(id);
        song.softDelete();
        songRepository.save(song);
    }

    public void deleteSongAsArtist(Long songId, Long userId){
        Artist artist = artistService.findByUserId(userId);
        Song song = findSongById(songId);

        if (song.getArtist() == null || !song.getArtist().getId().equals(artist.getId()))
            throw new UnauthorizedException("No tenes permiso para eliminar esta cancion");

        if (song.getStatus() == Status.APPROVED)
            throw new UnauthorizedException("No podes eliminar una cancion ya aprobada");

        deleteSong(songId);
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

    public List<SongResponse> findMySongs(Long userId) {
        Artist artist = artistService.findByUserId(userId);
        return songRepository.findByArtistId(artist.getId())
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
