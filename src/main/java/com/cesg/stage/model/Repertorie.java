package com.cesg.stage.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Document
@Setter
@Getter
@EqualsAndHashCode
public class Repertorie implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String repertorieName;
    private LocalDateTime creationDate = LocalDateTime.now();
    private Set<Song> songs = Collections.emptySet();

}
