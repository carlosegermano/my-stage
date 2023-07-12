package com.cesg.stage.services;

import com.cesg.stage.exceptions.DuplicatedNameException;
import com.cesg.stage.exceptions.ObjectNotFoundException;
import com.cesg.stage.model.Repertorie;
import com.cesg.stage.model.Song;
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
        Repertorie repertorie = this.getMoreRecent();
        Optional<Song> song = this.songRepository.findById(songId);
        if (song.isPresent()) {
            if (repertorie.getSongs().contains(song.get())) {
                throw new DuplicatedNameException("Música já existe no repertório atual");
            }
            song.get().setRepertorieId(repertorie.getId());
            this.songRepository.save(song.get());
            repertorie.getSongs().add(song.get());
            this.repertorieRepository.save(repertorie);
        }
    }

    @Transactional
    public void removeSong(String songId) {
        Repertorie repertorie = this.getMoreRecent();
        repertorie.getSongs().removeIf(s -> s.getId().equals(songId));
        this.songRepository.findById(songId).map(song -> {
            song.setRepertorieId("");
            this.songRepository.save(song);
            return song;
        });
    }
}
