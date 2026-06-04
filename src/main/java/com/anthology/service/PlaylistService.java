package com.anthology.service;

import com.anthology.dto.requests.PlaylistRequest;
import com.anthology.dto.requests.PlaylistUpdateRequest;
import com.anthology.dto.requests.UserUpdateRequest;
import com.anthology.dto.responses.PlaylistResponse;
import com.anthology.dto.responses.UserResponse;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.PlaylistMapper;
import com.anthology.model.Album;
import com.anthology.model.Playlist;
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

    public PlaylistResponse create(PlaylistRequest playlistRequest)
    {
        if(playlistRepository.exitsByName(playlistRequest.name()))
            throw new DuplicateResourceException("Ya existe un playlist con ese nombre");
        Playlist playlist=playlistMapper.toEntity(playlistRequest);
        return playlistMapper.toDto(playlistRepository.save(playlist));
    }
    public PlaylistResponse updatePlaylist(Long id, PlaylistUpdateRequest request)
    {
        Playlist playlist=findPlaylistById(id);
        if(request.name()!= null)playlist.setName(request.name());
       User user= userService.findUserById(request.idUser());
        if(request.idUser()!= null)playlist.setUser(user);


        return playlistMapper.toDto(playlistRepository.save(playlist));
    }
    public Playlist findPlaylistById(Long id){
        return playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrado"));
    }
    public List<PlaylistResponse> findAllPlaylist()
    {
        return playlistRepository.findAll().stream().map(playlistMapper::toDto).toList();

    }
    public PlaylistResponse findById(Long id)
    {
        return playlistMapper.toDto(findPlaylistById(id));
    }
    public void deleatePlaylist(Long id)
    {
        Playlist playlist=findPlaylistById(id);
        playlistRepository.delete(playlist);
    }
}
