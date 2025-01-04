package com.example.registers_api.services;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.utils.ExceptionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResearchLayerService {

    private final ResearchLayerRepository researchLayerRepository;
    private final ResearchLayerMapper researchLayerMapper;


    public ResearchLayerDTO saveResearchLayer(ResearchLayerDTO researchLayerDTO) {
        ResearchLayerCollection layerSaved = researchLayerRepository.save(researchLayerMapper.toCollection(researchLayerDTO));
        if(researchLayerDTO.getNombreCapa().trim().isEmpty() || researchLayerDTO.getDescripcion().trim().isEmpty()
                || researchLayerDTO.getJefeCapa().getNombre().isEmpty() || researchLayerDTO.getJefeCapa().getNumero_identificacion().isEmpty()){
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
        }
        else{
            return researchLayerMapper.toDto(layerSaved);
        }
    }
}
