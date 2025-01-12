package com.example.registers_api.mappers;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.models.ResearchLayerCollection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResearchLayerMapper{

    ResearchLayerCollection toCollection(ResearchLayerDTO dto);
    ResearchLayerDTO toDto(ResearchLayerCollection collection);
}
