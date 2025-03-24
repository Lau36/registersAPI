package com.example.registers_api.dtos;

import lombok.*;

@RequiredArgsConstructor
@Setter
@Getter
public class ResearchLayerDTO {

    private String id;
    private String layerName;
    private String description;
    private LayerBossDTO layerBoss;
}
