package com.example.registers_api.services;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.utils.ExceptionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchLayerService {

    private final ResearchLayerRepository researchLayerRepository;
    private final ResearchLayerMapper researchLayerMapper;


    public void saveResearchLayer(ResearchLayerDTO researchLayerDTO) {
        try {
            notEmptyValidations(researchLayerDTO);
            tooLongValidations(researchLayerDTO);
            alreadyExistsValidation(researchLayerDTO);

            researchLayerRepository.save(researchLayerMapper.toCollection(researchLayerDTO));

        } catch (Exception e) {
            System.out.println("Excepción capturada: " + e.getMessage());
            throw e;
        }
    }

    public ResearchLayerDTO getResearchLayerById(String researchLayerId) {
        ResearchLayerCollection layerCollection = researchLayerRepository.findById(researchLayerId).orElseThrow();
        return researchLayerMapper.toDto(layerCollection);
    }

    public List<ResearchLayerDTO> getAllResearchLayers() {
        List<ResearchLayerCollection> layerCollections = researchLayerRepository.findAll();
        return layerCollections.stream().map(researchLayerMapper::toDto).toList();
    }

    private void notEmptyValidations(ResearchLayerDTO researchLayerDTO) {
        if (researchLayerDTO.getNombreCapa().trim().isEmpty()
                || researchLayerDTO.getDescripcion().trim().isEmpty()
                || researchLayerDTO.getJefeCapa().getNombre().trim().isEmpty()
                || researchLayerDTO.getJefeCapa().getId() == 0
                || researchLayerDTO.getJefeCapa().getNumero_identificacion().trim().isEmpty()) {
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
        }
    }

    private void alreadyExistsValidation(ResearchLayerDTO researchLayerDTO) {
        boolean exists = researchLayerRepository.existsByNombreCapa(researchLayerDTO.getNombreCapa());
        if (exists) {
            throw new AlreadyExistsException(String.format(ExceptionConstants.ALREADY_RESEARCH_LAYER_NAME_EXIST_EXCEPTION, researchLayerDTO.getNombreCapa()));
        }
    }

    private void tooLongValidations(ResearchLayerDTO researchLayerDTO) {
        if (researchLayerDTO.getNombreCapa().length() > 100) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "nombre capa", 100));
        } else if (researchLayerDTO.getDescripcion().length() > 200) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "descripcion", 200));
        }
    }
}
