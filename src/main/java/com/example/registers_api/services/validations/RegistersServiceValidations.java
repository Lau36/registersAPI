package com.example.registers_api.services.validations;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.models.Variable;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.registers_api.utils.ExceptionConstants.*;

@AllArgsConstructor
@Component
public class RegistersServiceValidations {

    private final VariableRepository variableRepository;
    private final ResearchLayerRepository layerRepository;
    private final ResearchLayerRepository researchLayerRepository;

    public void validateVariablesAndResearchLayers(RegisterRequest registerRequest) {
        List<Variable> variables = registerRequest.getVariables();

        for (Variable variable : variables) {
            if(!researchLayerRepository.existsByNombreCapa(variable.getResearchLayerName())){
                throw (new DoesntExistsException(String.format(RESEARCH_LAYER_NAME_NOT_FOUND, variable.getResearchLayerName())));
            }
            else if (!researchLayerRepository.existsById(variable.getResearchLayerId())) {
                throw (new DoesntExistsException(String.format(RESEARCH_LAYER_ID_NOT_FOUND, variable.getResearchLayerId())));
            }
            else if(!variableRepository.existsByNombreVariable(variable.getName())){
                throw (new DoesntExistsException(String.format(VARIABLE_NOT_FOUND, variable.getName())));
            }
            else if (!variableRepository.existsById(variable.getId())) {
                throw (new DoesntExistsException(String.format(VARIABLE_ID_NOT_FOUND, variable.getId())));
            }
        }
    }

}
