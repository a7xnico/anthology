package com.anthology.service;

import com.anthology.dto.responses.SongVersionResponse;
import com.anthology.enums.Instrument;
import com.anthology.enums.NotificationType;
import com.anthology.enums.Status;
import com.anthology.exception.DuplicateResourceException;
import com.anthology.exception.ResourceNotFoundException;
import com.anthology.exception.UnauthorizedException;
import com.anthology.mapper.SongVersionMapper;
import com.anthology.model.Artist;
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
    private final CloudinaryService cloudinaryService;
    private final ArtistService artistService;
    private final NotificationService notificationService;

    private SongVersionResponse createVersion(Long songId, Instrument instrument, MultipartFile file, Status status) {
        Song song = songService.findSongById(songId);

        if (songVersionRepository.existsBySongIdAndInstrument(songId, instrument))
            throw new DuplicateResourceException("Ya existe una version para este instrumento");

        byte[] pdfBytes = museScoreService.convertToPdf(file);

        String pdfFilename = song.getTitle() + "_" + instrument + ".pdf";
        String pdfPublicId = cloudinaryService.uploadPdf(pdfBytes, pdfFilename);
        String pdfUrl = cloudinaryService.buildFileUrl(pdfPublicId);

        SongVersion version = new SongVersion();
        version.setSong(song);
        version.setInstrument(instrument);
        version.setStatus(status);
        version.setPdfPublicId(pdfPublicId);
        version.setPdfUrl(pdfUrl);

        return songVersionMapper.toDTO(songVersionRepository.save(version));
    }

    public SongVersionResponse createVersionAsAdmin(Long songId, Instrument instrument, MultipartFile file) {
        return createVersion(songId, instrument, file, Status.APPROVED);
    }

    public SongVersionResponse createVersionAsArtist(Long songId, Instrument instrument, MultipartFile file, Long userId) {
        Artist artist = artistService.findByUserId(userId);

        Song song = songService.findSongById(songId);

        if (!song.getArtist().getId().equals(artist.getId())) {
            throw new UnauthorizedException("No tenés permiso para agregar versiones a esta canción");
        }

        notificationService.create(
                "El artista " + artist.getStageName() + " subió una versión pendiente para: " + song.getTitle() + " (" + instrument + ")",
                NotificationType.SONG_VERSION_PENDING
        );

        return createVersion(songId, instrument, file, Status.PENDING);
    }

    public List<SongVersionResponse> findVersionsBySongId(Long songId){
        songService.findSongById(songId);
        return songVersionRepository.findBySongIdAndStatus(songId, Status.APPROVED)
                .stream()
                .map(songVersionMapper::toDTO)
                .toList();
    }

    public List<SongVersionResponse> findAllVersionsBySongId(Long songId){
        songService.findSongById(songId);
        return songVersionRepository.findBySongId(songId)
                .stream()
                .map(songVersionMapper::toDTO)
                .toList();
    }

    public SongVersionResponse findVersionById(Long songId, Long versionId){
        songService.findSongById(songId);
        SongVersion version = findSongVersionById(versionId);

        if (!version.getSong().getId().equals(songId))
            throw new ResourceNotFoundException("La versión no pertenece a esta canción");

        return songVersionMapper.toDTO(version);
    }

    public SongVersionResponse updateStatus(Long songId, Long versionId, Status status) {
        songService.findSongById(songId);
        SongVersion version = findSongVersionById(versionId);
        version.setStatus(status);
        return songVersionMapper.toDTO(songVersionRepository.save(version));
    }

    public List<SongVersionResponse> findByStatus(Status status) {
        return songVersionRepository.findByStatus(status)
                .stream()
                .map(songVersionMapper::toDTO)
                .toList();
    }

    public List<SongVersionResponse> findMyVersions(Long artistId) {
        return songVersionRepository.findByArtistId(artistId)
                .stream()
                .map(songVersionMapper::toDTO)
                .toList();
    }

    public void deleteVersion(Long songId, Long versionId) {
        songService.findSongById(songId);
        SongVersion version = findSongVersionById(versionId);
        version.softDelete();
        songVersionRepository.save(version);
    }

    public void deleteVersionAsArtist(Long songId, Long versionId, Long userId) {
        Artist artist = artistService.findByUserId(userId);
        SongVersion version = findSongVersionById(versionId);

        if (version.getSong().getArtist() == null ||
                !version.getSong().getArtist().getId().equals(artist.getId()))
            throw new UnauthorizedException("No tenés permiso para eliminar esta versión");

        if (version.getStatus() == Status.APPROVED)
            throw new UnauthorizedException("No podés eliminar una versión ya aprobada");

        deleteVersion(songId, versionId);
    }

    public List<SongVersionResponse> findDeletedVersionsBySongId(Long songId) {
        songService.findSongById(songId);
        return songVersionRepository.findDeletedBySongId(songId)
                .stream()
                .map(songVersionMapper::toDTO)
                .toList();
    }


    public SongVersion findSongVersionById(Long versionId) {
        return songVersionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Versión no encontrada"));
    }


}
