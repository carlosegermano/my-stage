package com.cesg.stage.records;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RepertorieDTO(String id, String repertorieName, LocalDateTime creationDate) {
}
