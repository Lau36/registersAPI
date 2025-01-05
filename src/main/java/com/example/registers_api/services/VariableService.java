package com.example.registers_api.services;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.mappers.VariableMapper;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.utils.ExceptionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariableService {

    private final VariableRepository variableRepository;
    private final ResearchLayerRepository layerRepository;
    private final VariableMapper variableMapper;

    public void saveVariable(VariableDTO variableDTO) {
        try {
            notEmptyValidations(variableDTO);
            tooLongValidations(variableDTO);
            validateResearchLayerId(variableDTO.getIdCapaInvestigacion());
            alreadyExistsValidation(variableDTO);

            
            VariableCollection variableCollection = variableMapper.toVariableCollection(variableDTO);
            
            variableRepository.save(variableCollection);

        } catch (Exception e) {
            System.out.println("Excepción capturada: " + e.getMessage());
            throw e;
        }
    }

    public VariableDTO getVariableById(String variableId) {
        VariableCollection variableCollection = variableRepository.findById(variableId).orElseThrow();
        return variableMapper.toVariableDTO(variableCollection);
    }

    public List<VariableDTO> getAllVariables() {
        List<VariableCollection> variablesCollection = variableRepository.findAll();
        return variablesCollection.stream().map(variableMapper::toVariableDTO).toList();
    }

    private void validateResearchLayerId(String researchLayerId) {
        boolean exists = layerRepository.existsById(researchLayerId);
        if (!exists) {
            throw new DoesntExistsException(String.format(ExceptionConstants.DOESNT_EXIST, researchLayerId));
        }
    }

    private void notEmptyValidations(VariableDTO variableDTO) {
        if (variableDTO.getNombreVariable().trim().isEmpty()
                || variableDTO.getIdCapaInvestigacion().trim().isEmpty()
                || variableDTO.getDescripcion().trim().isEmpty()
                || variableDTO.getTipo().trim().isEmpty()) {
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
          }
        }

    private void alreadyExistsValidation(VariableDTO variableDTO) {
       boolean exists = variableRepository.existsByNombreVariable(variableDTO.getNombreVariable());

       if (exists) {
           throw new AlreadyExistsException(String.format(ExceptionConstants.ALREADY_VARIABLE_NAME_EXIST_EXCEPTION, variableDTO.getNombreVariable()));
         }
       }

    private void tooLongValidations(VariableDTO variableDTO) {
       if (variableDTO.getNombreVariable().length() > 90) {
           throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "nombre variable", 90));
       }
       else if (variableDTO.getDescripcion().length() > 200) {
           throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "descripcion", 200));
       }
    }
}
