package com.example.registers_api.mappers;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.response.VariablesResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VariableMapper {
    VariablesResponse toVariableResponse(VariableCollection variableCollection);
    VariableCollection toVariableCollection(VariableDTO variableDTO);
}
