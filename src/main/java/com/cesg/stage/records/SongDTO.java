package com.cesg.stage.records;

import com.cesg.stage.enums.Tone;

public record SongDTO(String id, String songName, String songWriter, String artist, Tone tone, String releaseYear, Boolean isMarked) {
}
