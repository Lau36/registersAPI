package com.example.registers_api.services.validations;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.models.HealthProfessional;
import com.example.registers_api.models.Patient;
import com.example.registers_api.models.Variable;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.example.registers_api.utils.ExceptionConstants.*;

@AllArgsConstructor
@Component
public class RegistersServiceValidations {

    private final VariableRepository variableRepository;
    private final ResearchLayerRepository researchLayerRepository;

    public void validateVariablesAndResearchLayer(RegisterRequest registerRequest) {
        List<Variable> variables = registerRequest.getVariables();

        for (Variable variable : variables) {
            if (!researchLayerRepository.existsById(variable.getResearchLayerId())) {
                throw (new DoesntExistsException(String.format(RESEARCH_LAYER_ID_NOT_FOUND, variable.getResearchLayerId())));
            }
            if (!variableRepository.existsById(variable.getId())) {
                throw (new DoesntExistsException(String.format(VARIABLE_ID_NOT_FOUND, variable.getId())));
            }
        }
    }

    public void validateRegisterFields(RegisterRequest registerRequest) {
        List<Variable> variables = registerRequest.getVariables();
        HealthProfessional healthProfessional = registerRequest.getHealthProfessional();

        if (Objects.isNull(variables)) {
            throw new NotEmptyFieldException(NOT_EMPTY_HEALTH_PROFESIONAL_FIELD);
        }
        if (Objects.isNull(healthProfessional)) {
            throw new NotEmptyFieldException(NOT_EMPTY_VARIABLES);
        }

    }

}
