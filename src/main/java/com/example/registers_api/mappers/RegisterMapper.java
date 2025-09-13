package com.example.registers_api.mappers;

import com.example.registers_api.models.RegisterCollection;
import com.example.registers_api.models.ResearchLayerGroup;
import com.example.registers_api.models.Variable;
import com.example.registers_api.response.RegisterResponse2;
import com.example.registers_api.response.ResearchLayerGroupResponse;
import com.example.registers_api.response.VariableInRegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(source = "id", target = "registerId")
    @Mapping(source = "registerInfo", target = "registerInfo")
    RegisterResponse2 toRegisterResponse(RegisterCollection s);

    @Mapping(source = "variables", target = "variablesInfo")
    ResearchLayerGroupResponse toResearchLayerGroupResponse(ResearchLayerGroup researchLayerGroup);

    @Mapping(source = "id",   target = "variableId")
    @Mapping(source = "name", target = "variableName")
    @Mapping(source = "type", target = "variableType")
    VariableInRegisterResponse toVariableInRegisterResponse(Variable variable);
}
