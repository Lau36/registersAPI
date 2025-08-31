package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.NotEnabledException;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.LayerBoss;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.response.ResearchLayerResponse;
import com.example.registers_api.services.IResearchLayerService;
import com.example.registers_api.services.validations.ResearchLayerServiceValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.registers_api.utils.Constants.RESEARCH_LAYER_NOT_ENABLED;
import static com.example.registers_api.utils.Constants.RESEARCH_LAYER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ResearchLayerService implements IResearchLayerService {

    private final ResearchLayerRepository researchLayerRepository;
    private final ResearchLayerMapper researchLayerMapper;
    private final ResearchLayerServiceValidations researchLayerServiceValidations;
    private final RegisterRepository registerRepository;

    @Override
    public void saveResearchLayer(ResearchLayerDTO researchLayerDTO) {
        try {
            researchLayerServiceValidations.notEmptyValidations(researchLayerDTO);
            researchLayerServiceValidations.tooLongValidations(researchLayerDTO);
            researchLayerServiceValidations.alreadyExistsResearchLayerValidation(researchLayerDTO);
            researchLayerServiceValidations.validateLayerBoss(researchLayerDTO.getLayerBoss().getEmail());

            Optional<ResearchLayerCollection> existing = researchLayerRepository
                    .findByLayerNameAndIsEnabled(researchLayerDTO.getLayerName(), true);

            if (existing.isPresent()) {
                ResearchLayerCollection existingLayer = existing.get();
                if (Boolean.FALSE.equals(existingLayer.getIsEnabled())) {
                    existingLayer.setIsEnabled(true);
                    existingLayer.setUpdatedAt(LocalDateTime.now());
                    researchLayerRepository.save(existingLayer);
                } else {
                    throw new RuntimeException("La capa de investigación ya existe y está habilitada.");
                }
            } else {
                ResearchLayerCollection researchLayerCollection = researchLayerMapper.toCollection(researchLayerDTO);
                researchLayerCollection.setCreatedAt(LocalDateTime.now());
                researchLayerCollection.setIsEnabled(true);
                researchLayerRepository.save(researchLayerCollection);
            }

        } catch (Exception e) {
            System.out.println("Excepción capturada: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public ResearchLayerResponse getResearchLayerById(String researchLayerId) {
        ResearchLayerCollection layerCollection = researchLayerRepository.findByIdAndIsEnabled(researchLayerId, true)
                .orElseThrow( () ->
                        new DoesntExistsException(String.format(RESEARCH_LAYER_NOT_FOUND, researchLayerId))
                );
        return researchLayerMapper.toResponse(layerCollection);
    }

    @Override
    public List<ResearchLayerResponse> getAllResearchLayers() {
        List<ResearchLayerCollection> layerCollections = researchLayerRepository.findAllByIsEnabled(true);
        return layerCollections.stream().map(researchLayerMapper::toResponse).toList();
    }

    @Override
    public void deleteResearchLayer(String researchLayerId) {
        boolean existsRegister = registerRepository.existsByRegisterInfoResearchLayerId(researchLayerId);
        if(existsRegister){
            ResearchLayerCollection researchLayerCollection = researchLayerRepository.findById(researchLayerId)
                    .orElseThrow( () ->
                            new DoesntExistsException(String.format(RESEARCH_LAYER_NOT_FOUND, researchLayerId))
                    );
            researchLayerCollection.setIsEnabled(false);
            researchLayerRepository.save(researchLayerCollection);
        }
        else{
            researchLayerRepository.deleteById(researchLayerId);
        }

    }

    @Override
    public void updateResearchLayer(String researchLayerId, ResearchLayerDTO researchLayerDTO) {
        ResearchLayerCollection existsResearchLayer = researchLayerRepository.findById(researchLayerId)
                .orElseThrow(() ->
                        new DoesntExistsException(String.format(RESEARCH_LAYER_NOT_FOUND, researchLayerId))
                );

        if (existsResearchLayer.getIsEnabled()) {
            researchLayerDTO.setId(researchLayerId);
            researchLayerServiceValidations.alreadyExistsResearchLayerValidationUpdate(researchLayerDTO, existsResearchLayer);

            existsResearchLayer.setDescription(researchLayerDTO.getDescription());
            existsResearchLayer.setLayerName(researchLayerDTO.getLayerName());

            LayerBoss layerBoss = LayerBoss.builder()
                    .id(researchLayerDTO.getLayerBoss().getId())
                    .name(researchLayerDTO.getLayerBoss().getName())
                    .identificationNumber(researchLayerDTO.getLayerBoss().getIdentificationNumber())
                    .build();

            existsResearchLayer.setLayerBoss(layerBoss);
            existsResearchLayer.setUpdatedAt(LocalDateTime.now());

            researchLayerRepository.save(existsResearchLayer);
        } else {
            throw new NotEnabledException(String.format(RESEARCH_LAYER_NOT_ENABLED, researchLayerDTO.getId()));
        }
    }

}
