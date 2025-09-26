package com.example.registers_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VariableInfoDTO {
    private String variableId;
    private String variableName;
    private String variableType;
    private String valueAsString;
    private Double valueAsNumber;
}
