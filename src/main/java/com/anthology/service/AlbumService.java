package com.anthology.service;

import com.anthology.dto.requests.AlbumRequest;
import com.anthology.dto.requests.AlbumUpdateRequest;
import com.anthology.dto.responses.AlbumResponse;
import com.anthology.enums.NotificationType;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.AlbumMapper;
import com.anthology.model.Album;
import com.anthology.model.Artist;
import com.anthology.repository.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final ArtistService artistService;
    private final NotificationService notificationService;


    public AlbumResponse createAlbum(AlbumRequest request){
        if (albumRepository.existsByTitleAndArtistName(request.title(), request.artistName()))
            throw new DuplicateResourceException("Ya existe un álbum con ese título y artista");

        Album album = albumMapper.toEntity(request);
        return albumMapper.toDTO(albumRepository.save(album));
    }

    public AlbumResponse createAlbumAsArtist(AlbumRequest request, Long userId) {
        Artist artist = artistService.findByUserId(userId);

        if (albumRepository.existsByTitleAndArtistName(request.title(), request.artistName())) {
            throw new DuplicateResourceException("Ya existe un álbum con ese título y artista");
        }

        Album album = albumMapper.toEntity(request);
        album.setArtist(artist);
        album.setStatus(Status.PENDING);

        notificationService.create(
                "El artista " + artist.getStageName() + " creó un álbum pendiente: " + album.getTitle(),
                NotificationType.ALBUM_PENDING
        );

        return albumMapper.toDTO(albumRepository.save(album));
    }

    public AlbumResponse updateAlbum(Long id, AlbumUpdateRequest request) {
        Album album = findAlbumById(id);

        if (request.title() != null) album.setTitle(request.title());
        if (request.releaseYear() != null) album.setReleaseYear(request.releaseYear());
        if (request.artistName() != null) album.setArtistName(request.artistName());

        return albumMapper.toDTO(albumRepository.save(album));
    }

    public AlbumResponse updateStatus(Long id, Status status) {
        Album album = findAlbumById(id);
        album.setStatus(status);
        return albumMapper.toDTO(albumRepository.save(album));
    }

    public List<AlbumResponse> findAllAlbums(){
        return albumRepository.findAll()
                .stream()
                .map(albumMapper::toDTO)
                .toList();
    }

    public AlbumResponse findById(Long id){
        return albumMapper.toDTO(findAlbumById(id));
    }

    public void deleteAlbum(Long id){
        Album album = findAlbumById(id);
        albumRepository.delete(album);
    }

    public Album findAlbumById(Long id){
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Álbum no encontrado"));
    }

    public List<AlbumResponse> findMyAlbums(Long userId){
        Artist artist = artistService.findByUserId(userId);

        return albumRepository.findByArtistId(artist.getId())
                .stream()
                .map(albumMapper::toDTO)
                .toList();
    }

    public List<AlbumResponse> findByStatus(Status status) {
        return albumRepository.findByStatus(status)
                .stream()
                .map(albumMapper::toDTO)
                .toList();
    }
}
