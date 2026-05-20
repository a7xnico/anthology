package com.anthology.controller;

import com.anthology.dto.requests.SongRequest;
import com.anthology.dto.responses.SongResponse;
import com.anthology.service.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/songs")
@AllArgsConstructor
public class SongController {
    private final SongService songService;

    @PostMapping
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody SongRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.createSong(request));
    }


}
