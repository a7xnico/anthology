package com.anthology.service;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.requests.PlaylistUpdateRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.enums.Role;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.exception.UnauthorizedException;
import com.anthology.mapper.PlaylistMapper;
import com.anthology.model.CredentialsEntity;
import com.anthology.model.Playlist;
import com.anthology.model.SongVersion;
import com.anthology.model.User;
import com.anthology.repository.PlaylistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserService userService;
    private final PlaylistMapper playlistMapper;
    private final SongVersionService songVersionService;

    public PlaylistResponse create(PlaylistRequest playlistRequest, CredentialsEntity credentials) {
        User user = credentials.getUser();
        if (playlistRepository.existsByUserIdAndName(user.getId(), playlistRequest.name()))
            throw new DuplicateResourceException("Ya existe una playlist con ese nombre");

        Playlist playlist = playlistMapper.toEntity(playlistRequest);
        playlist.setUser(user);
        boolean hayDefault = playlistRepository.existsByUserIdAndIsDefaultTrue(user.getId());
        playlist.setIsDefault(!hayDefault);
        return playlistMapper.toDTO(playlistRepository.save(playlist));
    }

    public PlaylistResponse updatePlaylist(Long id, PlaylistUpdateRequest request, CredentialsEntity credentials) {
        Playlist playlist = findPlaylistById(id);
        checkOwner(playlist, credentials);
        if (!playlist.getName().equals(request.name())
                && playlistRepository.existsByUserIdAndName(playlist.getUser().getId(), request.name()))
            throw new DuplicateResourceException("Ya existe una playlist con ese nombre");

        playlist.setName(request.name());
        return playlistMapper.toDTO(playlistRepository.save(playlist));
    }

    public Playlist findPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrada"));
    }

    public List<PlaylistResponse> findAllPlaylist() {
        return playlistRepository.findAll().stream().map(playlistMapper::toDTO).toList();
    }

    public PlaylistResponse findById(Long id, CredentialsEntity credentials) {
        Playlist playlist = findPlaylistById(id);
        checkOwner(playlist, credentials);
        return playlistMapper.toDTO(playlist);
    }

    public void deletePlaylist(Long id, CredentialsEntity credentials) {
        Playlist playlist = findPlaylistById(id);
        checkOwner(playlist, credentials);
        playlistRepository.delete(playlist);
    }

    public List<PlaylistResponse> findByUser(Long id, CredentialsEntity credentials) {
        if (!credentials.hasRole(Role.ADMIN) && !credentials.getUser().getId().equals(id))
            throw new UnauthorizedException("Solo puedes ver tus propias playlists");
        User user = userService.findUserById(id);
        return user.getPlaylists().stream().map(playlistMapper::toDTO).toList();
    }

    public PlaylistResponse agregarCancion(Long idPlaylist, Long idSongVersion, CredentialsEntity credentials) {
        Playlist playlist = findPlaylistById(idPlaylist);
        checkOwner(playlist, credentials);
        SongVersion songVersion = songVersionService.findSongVersionById(idSongVersion);
        if (songVersion.getStatus() != Status.APPROVED)
            throw new ResourceNotFoundException("Versión no encontrada");

        boolean yaEsta = playlist.getSongVersions().stream()
                .anyMatch(v -> v.getId().equals(idSongVersion));
        if (!yaEsta) {
            playlist.getSongVersions().add(songVersion);
            playlistRepository.save(playlist);
        }
        return playlistMapper.toDTO(playlist);
    }

    public PlaylistResponse eliminarCancion(Long idPlaylist, Long idSongVersion, CredentialsEntity credentials) {
        Playlist playlist = findPlaylistById(idPlaylist);
        checkOwner(playlist, credentials);
        songVersionService.findSongVersionById(idSongVersion);
        playlist.getSongVersions().removeIf(v -> v.getId().equals(idSongVersion));
        playlistRepository.save(playlist);
        return playlistMapper.toDTO(playlist);
    }

    private void checkOwner(Playlist playlist, CredentialsEntity credentials) {
        if (!credentials.hasRole(Role.ADMIN)
                && !playlist.getUser().getId().equals(credentials.getUser().getId()))
            throw new UnauthorizedException("Esa playlist no es tuya");
    }

}
