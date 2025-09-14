package com.example.registers_api.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResearchLayerGroupRequest {
    private String researchLayerId;
    private String researchLayerName;
    private List<VariablesGroupRequest> variablesInfo;
}
