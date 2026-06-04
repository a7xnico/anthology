package com.anthology.service;

import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.enums.Instrument;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.mapper.SongVersionMapper;
import com.anthology.model.Song;
import com.anthology.model.SongVersion;
import com.anthology.repository.SongVersionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class SongVersionService {
    private final SongVersionRepository songVersionRepository;
    private final SongVersionMapper songVersionMapper;
    private final SongService songService;
    private final MuseScoreService museScoreService;
    private final GoogleDriveService googleDriveService;

    public SongVersionResponse createVersion(Long songId, Instrument instrument, MultipartFile file){
        Song song = songService.findSongById(songId);

        if (songVersionRepository.existsBySongIdAndInstrument(songId, instrument))
            throw new DuplicateResourceException("Ya existe una version para este instrumento");

        byte[] pdfBytes = museScoreService.convertToPdf(file);

        String pdfFilename = song.getTitle() + "_" + instrument + ".pdf";
        String pdfDriveId = googleDriveService.uploadPdf(pdfBytes, pdfFilename);
        String pdfUrl = googleDriveService.buildFileUrl(pdfDriveId);

        SongVersion version = new SongVersion();
        version.setSong(song);
        version.setInstrument(instrument);
        version.setStatus(Status.APPROVED);
        version.setPdfDriveId(pdfDriveId);
        version.setPdfUrl(pdfUrl);
        return songVersionMapper.toDTO(songVersionRepository.save(version));
    }

    public List<SongVersionResponse> findVersionsBySongId(Long songId){
        songService.findSongById(songId);
        return songVersionRepository.findBySongId(songId)
                .stream()
                .map(songVersionMapper::toDTO)
                .toList();
    }

    public SongVersionResponse findVersionById(Long songId, Long versionId){
        songService.findSongById(songId);
        return songVersionMapper.toDTO(findSongVersionById(versionId));
    }

    public SongVersionResponse updateStatus(Long songId, Long versionId, Status status) {
        songService.findSongById(songId);
        SongVersion version = findSongVersionById(versionId);
        version.setStatus(status);
        return songVersionMapper.toDTO(songVersionRepository.save(version));
    }

// seria usado para artistController creo yo
//    public List<SongVersionResponse> findMyVersions(Long artistId) {
//        return songVersionRepository.findByArtistId(artistId)
//                .stream()
//                .map(songVersionMapper::toDTO)
//                .toList();
//    }

    public void deleteVersion(Long songId, Long versionId) {
        songService.findSongById(songId);
        SongVersion version = findSongVersionById(versionId);
        version.softDelete();
        songVersionRepository.save(version);
    }


    public SongVersion findSongVersionById(Long versionId) {
        return songVersionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Versión no encontrada"));
    }


}
