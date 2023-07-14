package com.cesg.stage.services;

import com.cesg.stage.exceptions.BadRequestException;
import com.cesg.stage.exceptions.ObjectNotFoundException;
import com.cesg.stage.model.Repertorie;
import com.cesg.stage.model.Song;
import com.cesg.stage.model.User;
import com.cesg.stage.records.SongDTO;
import com.cesg.stage.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final RepertorieService repertorieService;
    private final UserService userService;

    public SongDTO saveSong(Song song) {
        User loggedUser = this.userService.getLoggedUser();
        song.setUserId(loggedUser.getId());
        song.setRepertorieId("");
        song.setIsMarked(Boolean.FALSE);
        Song songSaved = this.songRepository.save(song);
        return new SongDTO(
                songSaved.getId(),
                songSaved.getSongName(),
                songSaved.getSongWriter(),
                songSaved.getArtist(),
                songSaved.getTone(),
                songSaved.getReleaseYear(),
                songSaved.getIsMarked()
        );
    }

    public List<Song> findAll() {
        return this.songRepository.findAll();
    }

    public List<Song> findAllSongsOfCurrent() {
        Repertorie repertorie = this.repertorieService.getMoreRecent();
        return this.songRepository.findAllByRepertorieId(repertorie.getId());
    }

    public void deleteSong(String id) {

        User loggedUser = this.userService.getLoggedUser();

        this.songRepository.findById(id)
                .map(song -> {
                    if (!loggedUser.getId().equals(song.getUserId())) {
                        throw new BadRequestException("Só é possível excluir música que você adicionou!");
                    }
                    this.songRepository.deleteById(id);
                    return song;
                })
                .orElseThrow(() -> new ObjectNotFoundException("Música não encontrada!"));
    }
}
