package com.anthology.controller;

import com.anthology.dto.responses.SongResponse;
import com.anthology.service.SongService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@AllArgsConstructor
public class SongController {
    private final SongService songService;

    @GetMapping
    public ResponseEntity<List<SongResponse>> findSongs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String artistName){
        return ResponseEntity.status(HttpStatus.OK).body(songService.findSongs(title, genre, artistName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(songService.findById(id));
    }


}
