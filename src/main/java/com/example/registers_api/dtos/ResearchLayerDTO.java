package com.example.registers_api.dtos;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
public class ResearchLayerDTO {

    private final String id;
    private final String nombreCapa;
    private final String descripcion;
    private final LayerBossDTO jefeCapa;
}
