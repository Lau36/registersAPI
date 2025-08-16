package com.example.registers_api.response;

import com.example.registers_api.models.Variable;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ResearchLayerGroupResponse {
    private String researchLayerId;
    private String researchLayerName;
    private List<VariableInRegisterResponse> variablesInfo;
}
