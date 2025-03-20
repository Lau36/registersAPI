package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.mappers.VariableMapper;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.services.IVariableService;
import com.example.registers_api.services.validations.VariableServiceValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.registers_api.utils.Constants.VARIABLE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class VariableService implements IVariableService {

    private final VariableRepository variableRepository;
    private final ResearchLayerRepository layerRepository;
    private final VariableMapper variableMapper;
    private final VariableServiceValidations variableServiceValidations;

    @Override
    public void saveVariable(VariableDTO variableDTO) {
        try {
            variableServiceValidations.notEmptyValidations(variableDTO);
            variableServiceValidations.tooLongValidations(variableDTO);
            variableServiceValidations.validateResearchLayerId(variableDTO.getIdCapaInvestigacion());
            variableServiceValidations.alreadyExistsValidation(variableDTO);

            VariableCollection variableCollection = variableMapper.toVariableCollection(variableDTO);
            variableCollection.setTieneOpciones(hasOptionsItem(variableDTO));
            variableCollection.setFechaCreacion(LocalDateTime.now());
            
            variableRepository.save(variableCollection);

        } catch (Exception e) {
            System.out.println("Excepción capturada: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateVariable(String variableId, VariableDTO variableDTO) {
        variableDTO.setId(variableId);
        VariableCollection existsVariable = variableRepository.findById(variableId)
                .orElseThrow( () ->
                        new DoesntExistsException(String.format(VARIABLE_NOT_FOUND, variableDTO.getId()))
        );
        variableServiceValidations.validateResearchLayerId(variableDTO.getIdCapaInvestigacion());
        variableServiceValidations.alreadyExistsValidationUpdate(variableDTO, existsVariable);

        existsVariable.setNombreVariable(variableDTO.getNombreVariable());
        existsVariable.setDescripcion(variableDTO.getDescripcion());
        existsVariable.setOpciones(variableDTO.getOpciones());
        existsVariable.setIdCapaInvestigacion(variableDTO.getIdCapaInvestigacion());
        existsVariable.setTieneOpciones(hasOptionsItem(variableDTO));
        existsVariable.setFechaActualizacion(LocalDateTime.now());
        variableRepository.save(existsVariable);
    }

    @Override
    public List<VariableDTO> getAllVariablesById(String idCapaInvestigacion) {
        variableServiceValidations.validateResearchLayerId(idCapaInvestigacion);
        List<VariableCollection> variablesCollections = variableRepository.findAllByIdCapaInvestigacion(idCapaInvestigacion);
        return variablesCollections.stream().map(variableMapper::toVariableDTO).toList();
    }

    @Override
    public VariableDTO getVariableById(String variableId) {
        VariableCollection variableCollection = variableRepository.findById(variableId).orElseThrow();
        return variableMapper.toVariableDTO(variableCollection);
    }

    @Override
    public List<VariableDTO> getAllVariables() {
        List<VariableCollection> variablesCollection = variableRepository.findAll();
        return variablesCollection.stream().map(variableMapper::toVariableDTO).toList();
    }

    @Override
    public void deleteVariable(String variableId) {
        variableRepository.deleteById(variableId);
    }

    public boolean hasOptionsItem(VariableDTO variableDTO) {
        return !variableDTO.getOpciones().isEmpty();
    }


}
