package com.example.registers_api.services.validations;


import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VariableServiceValidationsTest {
    @Mock
    private ResearchLayerRepository layerRepository;

    @Mock
    private VariableRepository variableRepository;

    @InjectMocks
    private VariableServiceValidations validations;

    private VariableDTO variableDTO;

    @BeforeEach
    void setUp() {
        variableDTO = new VariableDTO();
        variableDTO.setVariableName("Nombre");
        variableDTO.setDescription("Descripción");
        variableDTO.setResearchLayerId("layerId");
        variableDTO.setType("String");
    }

    @Test
    void validateResearchLayerId_Exists_NoException() {
        when(layerRepository.existsById("layerId")).thenReturn(true);
        assertDoesNotThrow(() -> validations.validateResearchLayerId("layerId"));
    }

    @Test
    void validateResearchLayerId_NotExists_ThrowsException() {
        when(layerRepository.existsById("layerId")).thenReturn(false);
        assertThrows(DoesntExistsException.class, () -> validations.validateResearchLayerId("layerId"));
    }

    @Test
    void notEmptyValidations_ValidInput_NoException() {
        assertDoesNotThrow(() -> validations.notEmptyValidations(variableDTO));
    }

    @Test
    void notEmptyValidations_EmptyField_ThrowsException() {
        variableDTO.setVariableName(" ");
        assertThrows(NotEmptyFieldException.class, () -> validations.notEmptyValidations(variableDTO));
    }

    @Test
    void alreadyExistsValidation_NotExists_NoException() {
        when(variableRepository.existsByVariableNameAndIsEnabled("Nombre", true)).thenReturn(false);
        assertDoesNotThrow(() -> validations.alreadyExistsValidation(variableDTO));
    }

    @Test
    void alreadyExistsValidation_Exists_ThrowsException() {
        when(variableRepository.existsByVariableNameAndIsEnabled("Nombre", true)).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> validations.alreadyExistsValidation(variableDTO));
    }

    @Test
    void alreadyExistsValidationUpdate_SameId_NoException() {
        VariableCollection existing = new VariableCollection("ReseachLayerId", "Nombre", "Var descripción");
        existing.setId("123");

        VariableCollection found = new VariableCollection("ReseachLayerId", "Nombre", "Var descripción");
        found.setId("123");

        when(variableRepository.findByVariableName("Nombre")).thenReturn(Optional.of(found));

        variableDTO.setVariableName("Nombre");
        validations.alreadyExistsValidationUpdate(variableDTO, existing);
    }

    @Test
    void alreadyExistsValidationUpdate_DifferentId_ThrowsException() {
        VariableCollection existing = new VariableCollection("ReseachLayerId", "Nombre", "Var descripción");
        existing.setId("123");

        VariableCollection found = new VariableCollection("ReseachLayerId", "Nombre", "Var descripción");
        found.setId("456");

        when(variableRepository.findByVariableName("Nombre")).thenReturn(Optional.of(found));

        variableDTO.setVariableName("Nombre");

        assertThrows(AlreadyExistsException.class, () -> validations.alreadyExistsValidationUpdate(variableDTO, existing));
    }

    // ✅ tooLongValidations - OK
    @Test
    void lengthValidations_ValidLength_NoException() {
        variableDTO.setVariableName("a".repeat(90));
        variableDTO.setDescription("a".repeat(200));
        assertDoesNotThrow(() -> validations.lengthValidations(variableDTO));
    }

    // ✅ tooLongValidations - name too long
    @Test
    void tooLongValidations_NameLength_ThrowsException() {
        variableDTO.setVariableName("a".repeat(91));
        assertThrows(MaxLengthExceededException.class, () -> validations.lengthValidations(variableDTO));
    }

    @Test
    void tooLongValidations_DescriptionLength_ThrowsException() {
        variableDTO.setDescription("a".repeat(201));
        assertThrows(MaxLengthExceededException.class, () -> validations.lengthValidations(variableDTO));
    }
}
