package com.example.registers_api.services;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResearchLayerService {

    private final ResearchLayerRepository researchLayerRepository;
    private final ResearchLayerMapper researchLayerMapper;

    public String saveResearchLayer(ResearchLayerDTO researchLayerDTO) {
        ResearchLayerCollection layerSaved = researchLayerRepository.save(researchLayerMapper.toCollection(researchLayerDTO));
        if(layerSaved != null) {
            return "Research Layer Saved";
        }
        else {
            return "Research Layer Not Saved";
        }
    }
}
