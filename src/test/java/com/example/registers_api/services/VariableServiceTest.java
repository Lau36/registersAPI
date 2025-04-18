package com.example.registers_api.services;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.exceptions.*;
import com.example.registers_api.mappers.VariableMapper;
import com.example.registers_api.models.VariableCollection;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.response.VariablesResponse;
import com.example.registers_api.services.impl.VariableService;
import com.example.registers_api.services.validations.VariableServiceValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VariableServiceTest {
    @InjectMocks
    private VariableService variableService;

    @Mock
    private VariableRepository variableRepository;

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private VariableMapper variableMapper;

    @Mock
    private VariableServiceValidations variableServiceValidations;

    private VariableDTO variableDTO;
    private VariableCollection variableCollection;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        variableDTO = new VariableDTO();
        variableDTO.setVariableName("Variable name");
        variableDTO.setDescription("Variable description");
        variableDTO.setResearchLayerId("Id research layer");
        variableDTO.setOptions(List.of("Opction 1"));

        variableCollection = new VariableCollection("Id reseach layer", "Variable name", "Variable description");
        variableCollection.setId("var1");
        variableCollection.setOptions(List.of("Opction 1"));
        variableCollection.setIsEnabled(true);


    }

    // ✅ Test saveVariable - new variable
    @Test
    void testSaveNewVariable() {
        when(variableRepository.findByVariableName(variableDTO.getVariableName())).thenReturn(Optional.empty());
        when(variableMapper.toVariableCollection(variableDTO)).thenReturn(variableCollection);

        variableService.saveVariable(variableDTO);

        verify(variableServiceValidations, times(1)).notEmptyValidations(variableDTO);
        verify(variableServiceValidations , times(1)).lengthValidations(variableDTO);
        verify(variableServiceValidations, times(1)).validateResearchLayerId(variableDTO.getResearchLayerId());
        verify(variableRepository, times(1)).save(any(VariableCollection.class));
    }

    // ✅ Test saveVariable - re-enable disabled variable
    @Test
    void testSaveVariable_ReenableDisabled() {

        variableCollection.setIsEnabled(false);

        when(variableRepository.findByVariableName(variableCollection.getVariableName())).thenReturn(Optional.of(variableCollection));

        variableService.saveVariable(variableDTO);

        assertTrue(variableCollection.getIsEnabled());
        verify(variableRepository, times(1)).save(variableCollection);
    }

    // ✅ Test saveVariable - already enabled throws exception
    @Test
    void testSaveVariable_AlreadyEnabled_ThrowsException() {
        String varId = "id1";
        VariableDTO dto = new VariableDTO(varId, "ReseachLayerId", "Variable name", "Variable descripción", "String", List.of("Opt1"));
        VariableCollection existing = new VariableCollection("ReseachLayerId", "Variable name", "Var descripción");
        existing.setIsEnabled(true);

        when(variableRepository.findByVariableName(existing.getVariableName())).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(AlreadyExistsException.class, () -> {
            variableService.saveVariable(dto);
        });

        assertEquals("La variable ya existe y está habilitada.", ex.getMessage());
    }

    // ✅ Test updateVariable - happy path
    @Test
    void testUpdateVariable_Success() {
        String varId = "id1";
        VariableDTO dto = new VariableDTO(varId, "Desc", "layerId", "Variable descripción", "String", List.of("Opt1"));
        VariableCollection existing = new VariableCollection("ReseachLayerId", "Variable name", "Var descripción");
        existing.setIsEnabled(true);

        when(variableRepository.findById(varId)).thenReturn(Optional.of(existing));

        variableService.updateVariable(varId, dto);

        verify(variableRepository).save(any(VariableCollection.class));
    }

    // ✅ Test updateVariable - variable not enabled
    @Test
    void testUpdateVariable_NotEnabled_ThrowsException() {
        String varId = "id1";
        VariableDTO dto = new VariableDTO(varId, "Desc", "layerId", "Variable descripción", "String", List.of("Opt1"));
        VariableCollection existing = new VariableCollection("ReseachLayerId", "Variable name", "Var descripción");
        existing.setIsEnabled(false);

        when(variableRepository.findById(varId)).thenReturn(Optional.of(existing));

        assertThrows(NotEnabledException.class, () -> {
            variableService.updateVariable(varId, dto);
        });
    }

    @Test
    void testGetVariableById() {
        String varId = "var1";
        VariablesResponse response = new VariablesResponse();

        when(variableRepository.findByIdAndIsEnabled(varId, true)).thenReturn(Optional.of(variableCollection));
        when(variableMapper.toVariableResponse(variableCollection)).thenReturn(response);

        VariablesResponse result = variableService.getVariableById(varId);
        assertEquals(response, result);
    }

    @Test
    void testDeleteVariable_WithRegister() {
        String varId = "var1";
        variableCollection.setIsEnabled(true);

        when(registerRepository.existsByVariablesId(varId)).thenReturn(true);
        when(variableRepository.findById(varId)).thenReturn(Optional.of(variableCollection));

        variableService.deleteVariable(varId);
        assertFalse( variableCollection.getIsEnabled());
        verify(variableRepository, times(1)).save(variableCollection);
    }

    // ✅ Test deleteVariable - no register
    @Test
    void testDeleteVariable_WithoutRegister() {
        String varId = "var2";

        when(registerRepository.existsByVariablesId(varId)).thenReturn(false);

        variableService.deleteVariable(varId);

        verify(variableRepository, times(1)).deleteById(varId);
    }

    @Test
    void testDeleteVariable_doesntExist_ThrowsException() {
        String varId = "var2";

        when(registerRepository.existsByVariablesId(varId)).thenReturn(true);

        when(variableRepository.findById(varId)).thenReturn(Optional.empty());

        DoesntExistsException exception = assertThrows(DoesntExistsException.class, () ->
                variableService.deleteVariable(varId)
        );

        assertEquals("No se encontró una variable con el id: 'var2'", exception.getMessage());

        verify(variableRepository, times(1)).findById(varId);
    }


    @Test
    void getAllVariables() {
        when(variableRepository.findAllByIsEnabled(true)).thenReturn(List.of(variableCollection));

        variableService.getAllVariables();

        verify(variableRepository, times(1)).findAllByIsEnabled(true);
    }

    @Test
    void getVariableByIdFailed() {
        String varId = "var1";
        when(variableRepository.findByIdAndIsEnabled(varId, true)).thenReturn(Optional.empty());

        DoesntExistsException exception = assertThrows(DoesntExistsException.class, () ->
                variableService.getVariableById(varId)
        );
        assertEquals("No se encontró una variable con el id: 'var1'", exception.getMessage());

        verify(variableRepository, times(1)).findByIdAndIsEnabled(varId, true);
    }
}
