package com.cesg.stage.repositories;

import com.cesg.stage.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {
    List<Song> findAllByRepertorieId(String repertorieId);
}
