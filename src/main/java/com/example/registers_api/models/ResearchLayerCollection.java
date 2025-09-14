package com.example.registers_api.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@NoArgsConstructor
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
