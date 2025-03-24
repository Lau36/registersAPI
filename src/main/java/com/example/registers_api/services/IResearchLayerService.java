package com.example.registers_api.services;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.response.ResearchLayerResponse;

import java.util.List;

public interface IResearchLayerService {

    void saveResearchLayer(ResearchLayerDTO researchLayerDTO);

    ResearchLayerResponse getResearchLayerById(String researchLayerId);

    List<ResearchLayerResponse> getAllResearchLayers();

    void deleteResearchLayer(String researchLayerId);

    void updateResearchLayer(String researchLayerId, ResearchLayerDTO researchLayerDTO);
}
