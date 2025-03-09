package com.example.registers_api.services.validations;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.utils.ExceptionConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ResearchLayerServiceValidations {

    private final ResearchLayerRepository researchLayerRepository;

    public void notEmptyValidations(ResearchLayerDTO researchLayerDTO) {
        if (researchLayerDTO.getNombreCapa().trim().isEmpty()
                || researchLayerDTO.getDescripcion().trim().isEmpty()
                || researchLayerDTO.getJefeCapa().getNombre().trim().isEmpty()
                || researchLayerDTO.getJefeCapa().getId() == 0
                || researchLayerDTO.getJefeCapa().getNumero_identificacion().trim().isEmpty()) {
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
        }
    }

    public void alreadyExistsResearchLayerValidation(ResearchLayerDTO researchLayerDTO) {
        boolean exists = researchLayerRepository.existsByNombreCapa(researchLayerDTO.getNombreCapa());
        if (exists) {
            throw new AlreadyExistsException(String.format(ExceptionConstants.ALREADY_RESEARCH_LAYER_NAME_EXIST_EXCEPTION, researchLayerDTO.getNombreCapa()));
        }
    }

    public void tooLongValidations(ResearchLayerDTO researchLayerDTO) {
        if (researchLayerDTO.getNombreCapa().length() > 100) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "nombre capa", 100));
        } else if (researchLayerDTO.getDescripcion().length() > 200) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "descripcion", 200));
        }
    }
}
