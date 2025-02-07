package com.example.registers_api.services;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.models.ResearchLayerCollection;

import java.util.List;

public interface IResearchLayerService {

    void saveResearchLayer(ResearchLayerDTO researchLayerDTO);

    ResearchLayerDTO getResearchLayerById(String researchLayerId);

    List<ResearchLayerDTO> getAllResearchLayers();
}
