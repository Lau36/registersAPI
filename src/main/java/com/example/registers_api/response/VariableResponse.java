package com.example.registers_api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VariableResponse {
    private String variableId;
    private String variableName;
    private Object value;
    private String type;
    private String researchLayerId;
    private String researchLayerName;
}
