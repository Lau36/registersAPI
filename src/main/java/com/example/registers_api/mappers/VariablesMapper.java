package com.example.registers_api.mappers;

import com.example.registers_api.dtos.VariableInfoDTO;
import com.example.registers_api.models.Variable;

import java.util.List;
import java.util.stream.Collectors;

public class VariablesMapper {
    public static List<VariableInfoDTO> toVariablesInfo(List<Variable> vars) {
        if (vars == null) return List.of();
        return vars.stream().map(v ->
                VariableInfoDTO.builder()
                        .variableId(v.getId())
                        .variableName(v.getName())
                        .variableType(v.getType())
                        .valueAsString(v.getValueAsString())
                        .valueAsNumber(v.getValueAsNumber())
                        .build()
        ).collect(Collectors.toList());
    }
}
