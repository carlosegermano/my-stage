package com.cesg.stage.controllers;

import com.cesg.stage.model.Repertorie;
import com.cesg.stage.records.RepertorieDTO;
import com.cesg.stage.records.RepertorieUpdateDTO;
import com.cesg.stage.services.RepertorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/stage/repertories")
public class RepertorieController {

    private final RepertorieService repertorieService;

    @PostMapping
    public RepertorieDTO saveRepertorie(@RequestBody Repertorie repertorie) {
        return this.repertorieService.saveRepertorie(repertorie);
    }

    @PostMapping("/songs/{songId}")
    public void addSong(@PathVariable String songId) {
        this.repertorieService.addSong(songId);
    }

    @DeleteMapping("/songs/{songId}")
    public void removeSong(@PathVariable String songId) {
        this.repertorieService.removeSong(songId);
    }

    @PutMapping("/{repertorieId}")
    public RepertorieDTO updateRepertorie(@RequestBody RepertorieUpdateDTO repertorie, @PathVariable String repertorieId) {
        return this.repertorieService.updateRepertorie(repertorie, repertorieId);
    }

    @GetMapping(value = "/current")
    public Repertorie getMoreRecent() {
        return this.repertorieService.getMoreRecent();
    }

    @GetMapping
    public List<Repertorie> getRepertories() {
        return this.repertorieService.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteRepertorie(@PathVariable String id) {
        this.repertorieService.deleteRepertorie(id);
    }
}
