package com.cesg.stage.services;

import com.cesg.stage.model.Repertorie;
import com.cesg.stage.model.Song;
import com.cesg.stage.records.RepertorieDTO;
import com.cesg.stage.records.RepertorieUpdateDTO;

import java.util.List;

public sealed interface RepertorieService permits RepertorieServiceImpl {

    RepertorieDTO saveRepertorie(Repertorie repertorie);

    RepertorieDTO updateRepertorie(RepertorieUpdateDTO repertorie, String repertorieId);

    Repertorie getMoreRecent();

    List<Repertorie> findAll();

    void deleteRepertorie(String id);

    void addSong(String songId);

    void removeSong(String songId);
}
