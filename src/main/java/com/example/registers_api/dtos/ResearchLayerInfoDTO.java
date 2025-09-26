package com.example.registers_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearchLayerInfoDTO {
    private String researchLayerId;
    private String researchLayerName;
    private List<VariableInfoDTO> variablesInfo;
}
