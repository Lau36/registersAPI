package com.example.registers_api.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "researchLayerView")
public class ResearchLayerCollection {
    @Id
    private String id;

    @NonNull
    private String nombreCapa;

    @NonNull
    private String descripcion;

    @NonNull
    private LayerBoss jefeCapa;

}
