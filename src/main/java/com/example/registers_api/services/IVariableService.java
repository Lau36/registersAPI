package com.example.registers_api.services;

import com.example.registers_api.dtos.VariableDTO;

import java.util.List;

public interface IVariableService {

    void saveVariable(VariableDTO variableDTO);

    List<VariableDTO> getAllVariablesById(String idCapaInvestigacion);

    VariableDTO getVariableById(String variableId);

    List<VariableDTO> getAllVariables();
}
