package com.cesg.stage.controllers;

import com.cesg.stage.model.Song;
import com.cesg.stage.records.SongDTO;
import com.cesg.stage.services.RepertorieService;
import com.cesg.stage.services.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/stage/songs")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;
    private final RepertorieService repertorieService;

    @PostMapping
    public SongDTO saveSong(@RequestBody Song song) {
        return this.songService.saveSong(song);
    }

    @GetMapping
    public List<Song> findAll() {
        return this.songService.findAll();
    }

    @GetMapping(value = "/current")
    public List<Song> findAllOfCurrent() {
        return this.songService.findAllSongsOfCurrent();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteSong(@PathVariable String id) {
        this.songService.deleteSong(id);
    }
}
