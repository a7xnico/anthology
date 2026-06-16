package com.anthology.service;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.requests.PlaylistUpdateRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.PlaylistMapper;
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

    public PlaylistResponse create(PlaylistRequest playlistRequest) {
        if (playlistRepository.existsByName(playlistRequest.name()))
            throw new DuplicateResourceException("Ya existe un playlist con ese nombre");

        User user = userService.findUserById(playlistRequest.idUser());

        Playlist playlist=playlistMapper.toEntity(playlistRequest);
        playlist.setUser(user);
        return playlistMapper.toDTO(playlistRepository.save(playlist));
    }

    public PlaylistResponse updatePlaylist(Long id, PlaylistUpdateRequest request) {
        Playlist playlist = findPlaylistById(id);
        if (request.name() != null) playlist.setName(request.name());


        if (request.idUser() != null) {
            User user = userService.findUserById(request.idUser());
            playlist.setUser(user);
        }


       return playlistMapper.toDTO(playlistRepository.save(playlist));
    }

    public Playlist findPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrado"));
    }


    public List<PlaylistResponse> findAllPlaylist() {
        return playlistRepository.findAll().stream().map(playlistMapper::toDTO).toList();

    }

    public PlaylistResponse findById(Long id) {
        return playlistMapper.toDTO(findPlaylistById(id));
    }

    public void deletePlaylist(Long id) {
        Playlist playlist = findPlaylistById(id);
        playlistRepository.delete(playlist);
    }

    public List<PlaylistResponse> findByUser(Long id)
    {
        User user=userService.findUserById(id);
        return  user.getPlaylists().stream().map(playlistMapper::toDTO).toList();

    }
    public PlaylistResponse agregarCancion(Long idPlaylist,Long idSongVersion)
    {
        SongVersion songVersion=songVersionService.findSongVersionById(idSongVersion);
        Playlist playlist=findPlaylistById(idPlaylist);


        playlist.getSongVersions().add(songVersion);
        playlistRepository.save(playlist);

        return playlistMapper.toDTO(playlist);
    }
    public PlaylistResponse eliminarCancion(Long idPlaylist,Long idSongVersion)

    {
        SongVersion songVersion=songVersionService.findSongVersionById(idSongVersion);
        Playlist playlist=findPlaylistById(idPlaylist);
         playlist.getSongVersions().remove(songVersion);
         playlistRepository.save(playlist);
         return playlistMapper.toDTO(playlist);
    }

}
