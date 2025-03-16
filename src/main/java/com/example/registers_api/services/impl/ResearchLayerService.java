package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.LayerBoss;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.services.IResearchLayerService;
import com.example.registers_api.services.validations.ResearchLayerServiceValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.registers_api.utils.Constants.RESEARCH_LAYER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ResearchLayerService implements IResearchLayerService {

    private final ResearchLayerRepository researchLayerRepository;
    private final ResearchLayerMapper researchLayerMapper;
    private final ResearchLayerServiceValidations researchLayerServiceValidations;

    @Override
    public void saveResearchLayer(ResearchLayerDTO researchLayerDTO) {
        try {
            researchLayerServiceValidations.notEmptyValidations(researchLayerDTO);
            researchLayerServiceValidations.tooLongValidations(researchLayerDTO);
            researchLayerServiceValidations.alreadyExistsResearchLayerValidation(researchLayerDTO);

            ResearchLayerCollection researchLayerCollection = researchLayerMapper.toCollection(researchLayerDTO);
            researchLayerCollection.setFechaCreacion(LocalDateTime.now());

            researchLayerRepository.save(researchLayerCollection);

        } catch (Exception e) {
            System.out.println("Excepción capturada: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ResearchLayerDTO getResearchLayerById(String researchLayerId) {
        ResearchLayerCollection layerCollection = researchLayerRepository.findById(researchLayerId).orElseThrow();
        return researchLayerMapper.toDto(layerCollection);
    }

    @Override
    public List<ResearchLayerDTO> getAllResearchLayers() {
        List<ResearchLayerCollection> layerCollections = researchLayerRepository.findAll();
        return layerCollections.stream().map(researchLayerMapper::toDto).toList();
    }

    @Override
    public void deleteResearchLayer(String researchLayerId) {
        researchLayerRepository.deleteById(researchLayerId);
    }

    @Override
    public void updateResearchLayer(String researchLayerId, ResearchLayerDTO researchLayerDTO) {
        ResearchLayerCollection existsResearchLayer = researchLayerRepository.findById(researchLayerId)
                .orElseThrow( () ->
                        new DoesntExistsException(String.format(RESEARCH_LAYER_NOT_FOUND, researchLayerDTO.getId()))
                );
        researchLayerServiceValidations.alreadyExistsResearchLayerValidation(researchLayerDTO);

        existsResearchLayer.setDescripcion(researchLayerDTO.getDescripcion());
        existsResearchLayer.setNombreCapa(researchLayerDTO.getNombreCapa());

        LayerBoss layerBoss = LayerBoss.builder()
                .id(researchLayerDTO.getJefeCapa().getId())
                .nombre(researchLayerDTO.getJefeCapa().getNombre())
                .numeroIdentificacion(researchLayerDTO.getJefeCapa().getNumeroIdentificacion())
                .build();

        existsResearchLayer.setJefeCapa(layerBoss);
        existsResearchLayer.setFechaActualizacion(LocalDateTime.now());

        researchLayerRepository.save(existsResearchLayer);
    }

}
