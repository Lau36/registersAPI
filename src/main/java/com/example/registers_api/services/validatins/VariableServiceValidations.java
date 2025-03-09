package com.example.registers_api.services.validatins;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.utils.ExceptionConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class VariableServiceValidations {

    private final ResearchLayerRepository layerRepository;
    private final VariableRepository variableRepository;

    public void validateResearchLayerId(String researchLayerId) {
        boolean exists = layerRepository.existsById(researchLayerId);
        if (!exists) {
            throw new DoesntExistsException(String.format(ExceptionConstants.DOESNT_EXIST, researchLayerId));
        }
    }

    public void notEmptyValidations(VariableDTO variableDTO) {
        if (variableDTO.getNombreVariable().trim().isEmpty()
                || variableDTO.getIdCapaInvestigacion().trim().isEmpty()
                || variableDTO.getDescripcion().trim().isEmpty()
                || variableDTO.getTipo().trim().isEmpty()) {
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
        }
    }

    public void alreadyExistsValidation(VariableDTO variableDTO) {
        boolean exists = variableRepository.existsByNombreVariable(variableDTO.getNombreVariable());

        if (exists) {
            throw new AlreadyExistsException(
                    String.format(
                            ExceptionConstants.ALREADY_VARIABLE_NAME_EXIST_EXCEPTION, variableDTO.getNombreVariable())
            );
        }
    }

    public void tooLongValidations(VariableDTO variableDTO) {
        if (variableDTO.getNombreVariable().length() > 90) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "nombre variable", 90));
        }
        else if (variableDTO.getDescripcion().length() > 200) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "descripcion", 200));
        }
    }
}
