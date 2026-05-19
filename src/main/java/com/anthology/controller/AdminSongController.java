package com.anthology.controller;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.requests.SongUpdateRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.service.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/songs")
@AllArgsConstructor
public class AdminSongController {
    private final SongService songService;

    @PostMapping
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody SongRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.createSong(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SongResponse> updateSong(@PathVariable Long id,
                                                   @Valid @RequestBody SongUpdateRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(songService.updateSong(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id){
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SongResponse>> findAllSongs(){
        return ResponseEntity.status(HttpStatus.OK).body(songService.findAllSongs());
    }


}
