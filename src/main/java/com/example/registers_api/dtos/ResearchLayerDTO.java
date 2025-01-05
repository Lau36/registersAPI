package com.example.registers_api.dtos;

import lombok.*;

@RequiredArgsConstructor
@Setter
@Getter
public class ResearchLayerDTO {

    private String id;
    private String nombreCapa;
    private String descripcion;
    private LayerBossDTO jefeCapa;
}
