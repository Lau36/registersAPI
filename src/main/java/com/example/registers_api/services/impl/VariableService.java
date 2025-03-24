package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.NotEnabledException;
import com.example.registers_api.mappers.VariableMapper;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.response.VariablesResponse;
import com.example.registers_api.services.IVariableService;
import com.example.registers_api.services.validations.VariableServiceValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.registers_api.utils.Constants.VARIABLE_NOT_ENABLED;
import static com.example.registers_api.utils.Constants.VARIABLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class VariableService implements IVariableService {

    private final VariableRepository variableRepository;
    private final VariableMapper variableMapper;
    private final VariableServiceValidations variableServiceValidations;
    private final RegisterRepository registerRepository;

    @Override
    public void saveVariable(VariableDTO variableDTO) {
        try {
            variableServiceValidations.notEmptyValidations(variableDTO);
            variableServiceValidations.tooLongValidations(variableDTO);
            variableServiceValidations.validateResearchLayerId(variableDTO.getResearchLayerId());
            variableServiceValidations.alreadyExistsValidation(variableDTO);

            Optional<VariableCollection> existing = variableRepository.findByVariableName(variableDTO.getVariableName());

            if (existing.isPresent()) {
                VariableCollection existingVariable = existing.get();
                if (!existingVariable.getIsEnabled()) {
                    existingVariable.setIsEnabled(true);
                    existingVariable.setUpdatedAt(LocalDateTime.now());
                    variableRepository.save(existingVariable);
                } else {
                    throw new RuntimeException("La variable ya existe y está habilitada.");
                }
            } else {
                VariableCollection variableCollection = variableMapper.toVariableCollection(variableDTO);
                variableCollection.setHasOptions(hasOptionsItem(variableDTO));
                variableCollection.setCreatedAt(LocalDateTime.now());
                variableCollection.setIsEnabled(true); 
                variableRepository.save(variableCollection);
            }

        } catch (Exception e) {
            System.out.println("Excepción capturada: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateVariable(String variableId, VariableDTO variableDTO) {
        VariableCollection variableCollection = variableRepository.findById(variableId).orElseThrow();
        if(variableCollection.getIsEnabled()) {
            variableDTO.setId(variableId);
            VariableCollection existsVariable = variableRepository.findById(variableId)
                    .orElseThrow(() ->
                            new DoesntExistsException(String.format(VARIABLE_NOT_FOUND, variableDTO.getId()))
                    );
            variableServiceValidations.validateResearchLayerId(variableDTO.getResearchLayerId());
            variableServiceValidations.alreadyExistsValidationUpdate(variableDTO, existsVariable);

            setAtributtesAndSave(existsVariable, variableDTO);
        }
        else{
            throw new NotEnabledException(String.format(VARIABLE_NOT_ENABLED, variableDTO.getId()));
        }
    }

    @Override
    public List<VariablesResponse> getAllVariablesById(String idCapaInvestigacion) {
        variableServiceValidations.validateResearchLayerId(idCapaInvestigacion);
        List<VariableCollection> variablesCollections =
                variableRepository.findAllByResearchLayerIdAndIsEnabled(idCapaInvestigacion, true);
        return variablesCollections.stream().map(variableMapper::toVariableResponse).toList();
    }

    @Override
    public VariablesResponse getVariableById(String variableId) {
        VariableCollection variableCollection = variableRepository.findByIdAndIsEnabled(variableId, true)
                .orElseThrow(() ->
                        new DoesntExistsException(String.format(VARIABLE_NOT_FOUND, variableId))
                );
        return variableMapper.toVariableResponse(variableCollection);
    }

    @Override
    public List<VariablesResponse> getAllVariables() {
        List<VariableCollection> variablesCollection = variableRepository.findAllByIsEnabled(true);
        return variablesCollection.stream().map(variableMapper::toVariableResponse).toList();
    }

    @Override
    public void deleteVariable(String variableId) {

        boolean existsRegister = registerRepository.existsByVariableId(variableId);
        if(existsRegister){
            VariableCollection variableCollection = variableRepository.findById(variableId).orElseThrow(() ->
                    new DoesntExistsException(String.format(VARIABLE_NOT_FOUND, variableId))
            );
            variableCollection.setIsEnabled(false);
            variableRepository.save(variableCollection);
        }
        else{
            variableRepository.deleteById(variableId);
        }
    }

    public boolean hasOptionsItem(VariableDTO variableDTO) {
        return !variableDTO.getOptions().isEmpty();
    }

    public void setAtributtesAndSave(VariableCollection existsVariable, VariableDTO variableDTO) {
        existsVariable.setVariableName(variableDTO.getVariableName());
        existsVariable.setDescription(variableDTO.getDescription());
        existsVariable.setOptions(variableDTO.getOptions());
        existsVariable.setResearchLayerId(variableDTO.getResearchLayerId());
        existsVariable.setHasOptions(hasOptionsItem(variableDTO));
        existsVariable.setUpdatedAt(LocalDateTime.now());
        existsVariable.setIsEnabled(true);
        variableRepository.save(existsVariable);
    }


}
