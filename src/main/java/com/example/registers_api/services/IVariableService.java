package com.example.registers_api.services;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.response.VariablesResponse;

import java.util.List;

public interface IVariableService {

    void saveVariable(VariableDTO variableDTO);

    void updateVariable(String variableId, VariableDTO variableDTO);

    List<VariablesResponse> getAllVariablesById(String idCapaInvestigacion);

    VariablesResponse getVariableById(String variableId);

    List<VariablesResponse> getAllVariables();

    void deleteVariable(String variableId);
}
