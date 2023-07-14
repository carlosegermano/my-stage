package com.cesg.stage.services;

import com.cesg.stage.exceptions.BadRequestException;
import com.cesg.stage.exceptions.DuplicatedNameException;
import com.cesg.stage.exceptions.ObjectNotFoundException;
import com.cesg.stage.model.Repertorie;
import com.cesg.stage.model.Song;
import com.cesg.stage.model.User;
import com.cesg.stage.records.RepertorieDTO;
import com.cesg.stage.records.RepertorieUpdateDTO;
import com.cesg.stage.repositories.RepertorieRepository;
import com.cesg.stage.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class RepertorieServiceImpl implements RepertorieService {

    private final RepertorieRepository repertorieRepository;
    private final SongRepository songRepository;

    private final UserService userService;

    public RepertorieDTO saveRepertorie(Repertorie repertorie) {
        // Verificar se o usuário é um músico
        if (!this.findAll().isEmpty() && this.getMoreRecent().getRepertorieName().equalsIgnoreCase(repertorie.getRepertorieName())) {
            throw new DuplicatedNameException("O repertório atual possui o mesmo nome!");
        }
        Repertorie saved = this.repertorieRepository.save(repertorie);
        return new RepertorieDTO(saved.getId(), saved.getRepertorieName(), saved.getCreationDate());
    }

    @Override
    public RepertorieDTO updateRepertorie(RepertorieUpdateDTO repertorie, String repertorieId) {
            return this.repertorieRepository.findById(repertorieId)
                .map(rep -> {
                    rep.setRepertorieName(repertorie.repertorieName());
                    this.repertorieRepository.save(rep);
                    return RepertorieDTO.builder()
                            .id(rep.getId())
                            .repertorieName(rep.getRepertorieName())
                            .creationDate(rep.getCreationDate())
                            .build();
                })
                .orElseThrow(() -> new ObjectNotFoundException("Repertório não encontrado!"));
    }

    public Repertorie getMoreRecent() {
        Repertorie repertorie = this.repertorieRepository.findFirstByOrderByCreationDateDesc();
        if (repertorie == null) {
            throw new ObjectNotFoundException("Nenhum repertório criado!");
        }
        return repertorie;
    }

    public List<Repertorie> findAll() {
        return this.repertorieRepository.findAll();
    }

    @Transactional
    public void deleteRepertorie(String id) {
        this.repertorieRepository.findById(id)
                .map(repertorie -> {
                    for (Song song : repertorie.getSongs()) {
                        this.songRepository.deleteById(song.getId());
                    }
                    return repertorie;
                })
                .orElseThrow(() -> new ObjectNotFoundException("Repertório não existe!"));
        this.repertorieRepository.deleteById(id);
    }

    @Transactional
    public void addSong(String songId) {
        User loggedUser = this.userService.getLoggedUser();
        Repertorie repertorie = this.getMoreRecent();
        Optional<Song> songOptional = this.songRepository.findById(songId);
        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            if (repertorie.getSongs().contains(song)) {
                throw new DuplicatedNameException("Música já existe no repertório atual");
            }
            song.setRepertorieId(repertorie.getId());
            song.setUserId(loggedUser.getId());
            song.setIsMarked(true);
            this.songRepository.save(song);
            repertorie.getSongs().add(song);
            this.repertorieRepository.save(repertorie);
        }
    }

    @Transactional
    public void removeSong(String songId) {
        User loggedUser = this.userService.getLoggedUser();
        Repertorie repertorie = this.getMoreRecent();
        this.songRepository.findById(songId).map(song -> {

            if (loggedUser.getId().equals(song.getUserId())) {
                repertorie.getSongs().removeIf(s -> s.getId().equals(songId));
            } else {
                throw new BadRequestException("Só é possível remover uma música adicionada por você!");
            }

            song.setRepertorieId("");
            song.setUserId("");
            song.setIsMarked(false);
            this.songRepository.save(song);
            return song;
        });
    }
}
