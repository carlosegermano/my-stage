package com.cesg.stage.services;

import com.cesg.stage.model.Song;
import com.cesg.stage.records.SongDTO;

import java.util.List;

public sealed interface SongService permits SongServiceImpl {

    public SongDTO saveSong(Song song);

    List<Song> findAll();

    List<Song> findAllSongsOfCurrent();

    void deleteSong(String id);
}
