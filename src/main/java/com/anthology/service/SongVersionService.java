package com.anthology.service;

import com.anthology.dto.requests.SongVersionRequest;
import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.mapper.SongVersionMapper;
import com.anthology.model.Song;
import com.anthology.model.SongVersion;
import com.anthology.repository.SongVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class SongVersionService {
    private final SongVersionRepository songVersionRepository;
    private final SongVersionMapper songVersionMapper;
    private final SongService songService;

    public SongVersionResponse createVersion(Long songId, SongVersionRequest request, MultipartFile file){
        Song song = songService.findSongById(songId);

        if (songVersionRepository.existsBySongIdAndInstrument(songId, request.instrument()))
            throw new DuplicateResourceException("Ya existe una version para este instrumento");

        SongVersion version = new SongVersion();
        version.setSong(song);
        version.setInstrument(request.instrument());
        version.setStatus(Status.APPROVED);

        // ACA VA LO RELACIONADO A CONVERTIR EL ARCHIVO MusicXML o GuitarPro

        return songVersionMapper.toDTO(songVersionRepository.save(version));
    }
}
