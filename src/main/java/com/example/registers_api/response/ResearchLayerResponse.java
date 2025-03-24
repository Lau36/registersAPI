package com.example.registers_api.response;

import com.example.registers_api.models.LayerBoss;
import lombok.*;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResearchLayerResponse {

    private String id;
    private String layerName;
    private String description;
    private LayerBoss layerBoss;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
