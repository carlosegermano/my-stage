package com.cesg.stage.repositories;

import com.cesg.stage.model.Repertorie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepertorieRepository extends MongoRepository<Repertorie, String> {

    Repertorie findFirstByOrderByCreationDateDesc();
}
