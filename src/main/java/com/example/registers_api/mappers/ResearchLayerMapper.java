package com.example.registers_api.mappers;

import com.example.registers_api.dtos.LayerBossDTO;
import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.models.LayerBoss;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.response.ResearchLayerResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResearchLayerMapper{

    ResearchLayerCollection toCollection(ResearchLayerDTO dto);
    ResearchLayerResponse toResponse(ResearchLayerCollection collection);

}
