package com.example.registers_api.dtos;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ResearchLayerDTO {

    private final String nombreCapa;
    private final String descripcion;
    private final LayerBossDTO jefeCapa;
}
