package com.example.registers_api.mappers;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.models.VariableCollection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariableMapper {
    VariableDTO toVariableDTO(VariableCollection variableCollection);
    VariableCollection toVariableCollection(VariableDTO variableDTO);
}
