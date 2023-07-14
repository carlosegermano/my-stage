package com.cesg.stage.model;

import com.cesg.stage.enums.Tone;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class Song implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String songName;
    private String songWriter;
    private String artist;
    private Tone tone;
    private String releaseYear;
    private String repertorieId;
    private String userId;
    private Boolean isMarked;
}
