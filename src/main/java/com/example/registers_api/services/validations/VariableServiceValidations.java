package com.example.registers_api.services.validations;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.models.VariableCollection;
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
        if (variableDTO.getVariableName().trim().isEmpty()
                || variableDTO.getResearchLayerId().trim().isEmpty()
                || variableDTO.getDescription().trim().isEmpty()
                || variableDTO.getType().trim().isEmpty()) {
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
        }
    }

    public void alreadyExistsValidation(VariableDTO variableDTO) {
        boolean exists = variableRepository.existsByVariableNameAndIsEnabled(variableDTO.getVariableName(), true);

        if (exists) {
            throw new AlreadyExistsException(
                    String.format(
                            ExceptionConstants.ALREADY_VARIABLE_NAME_EXIST_EXCEPTION, variableDTO.getVariableName())
            );
        }
    }

    public void alreadyExistsValidationUpdate(VariableDTO currentVariable, VariableCollection existingVariable) {

        variableRepository.findByVariableName(currentVariable.getVariableName())
                .ifPresent(foundVariable -> {
                    if (!foundVariable.getId().equals(existingVariable.getId())) {
                        throw new AlreadyExistsException(
                                String.format(
                                        ExceptionConstants.ALREADY_VARIABLE_NAME_EXIST_EXCEPTION,
                                        currentVariable.getVariableName()
                                )
                        );
                    }
                });

    }

    public void tooLongValidations(VariableDTO variableDTO) {
        if (variableDTO.getVariableName().length() > 90) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "name variable", 90));
        }
        else if (variableDTO.getDescription().length() > 200) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "description", 200));
        }
    }
}
