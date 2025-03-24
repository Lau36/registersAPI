package com.example.registers_api.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "researchLayerView")
public class ResearchLayerCollection {
    @Id
    private String id;

    @NonNull
    private String layerName;

    @NonNull
    private String description;

    @NonNull
    private LayerBoss layerBoss;

    private Boolean isEnabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
