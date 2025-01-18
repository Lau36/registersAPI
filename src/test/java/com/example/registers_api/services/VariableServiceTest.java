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
    @Mock
    private VariableRepository variableRepository;

    @Mock
    private ResearchLayerRepository researchLayerRepository;

    @Mock
    private VariableMapper variableMapper;

    @InjectMocks
    private VariableService variableService;

    private VariableDTO variableDTO;
    private VariableCollection variableCollection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        variableDTO = new VariableDTO();
        variableDTO.setIdCapaInvestigacion("idCapaInvestigacion");
        variableDTO.setNombreVariable("nombreVariable");
        variableDTO.setDescripcion("descripcion");
        variableDTO.setTipo("tipo");

        variableCollection = new VariableCollection("idCapaInvestigacion", "nombreVariable", "descripcion", "tipo");

    }

    @Test
    void saveVariable_susccessfully(){
        when(variableMapper.toVariableCollection(variableDTO)).thenReturn(variableCollection);
        when(variableRepository.existsByNombreVariable(variableDTO.getNombreVariable())).thenReturn(false);
        when(researchLayerRepository.existsById(variableDTO.getIdCapaInvestigacion())).thenReturn(true);

        variableService.saveVariable(variableDTO);

        verify(variableRepository, times(1)).save(variableCollection);
    }

    @Test
    void saveVariable_AlreadyExists(){
        when(variableRepository.existsByNombreVariable(variableDTO.getNombreVariable())).thenReturn(true);
        when(researchLayerRepository.existsById(variableDTO.getIdCapaInvestigacion())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> variableService.saveVariable(variableDTO));

        assertEquals("Ya existe una variable con el nombre 'nombreVariable'", exception.getMessage());
        verify(variableRepository, never()).save(variableCollection);
    }

    @Test
    void saveVariable_doesNotExistsResearchLayerId(){
        when(variableRepository.existsByNombreVariable(variableDTO.getNombreVariable())).thenReturn(false);
        when(researchLayerRepository.existsById(variableDTO.getIdCapaInvestigacion())).thenReturn(false);

        DoesntExistsException exception = assertThrows(DoesntExistsException.class, () -> variableService.saveVariable(variableDTO));

        assertEquals("El campo idCapaInvestigacion no existe", exception.getMessage());
        verify(variableRepository, never()).save(variableCollection);
    }

    @Test
    void saveVariable_TooLongFields_description(){
        variableDTO.setDescripcion("a".repeat(201));
        maxLengthEsceeded_exception("El campo descripcion no puede exceder los 200 caracteres");
    }

    @Test
    void saveVariable_TooLongFields_name(){
        variableDTO.setNombreVariable("a".repeat(201));
        maxLengthEsceeded_exception("El campo nombre variable no puede exceder los 90 caracteres");
    }

    @Test
    void saveVariable_NotEmptyFields_description(){
        variableDTO.setDescripcion(" ");
        emptyFields_exception();
    }

    @Test
    void saveVariable_NotEmptyFields_name(){
        variableDTO.setNombreVariable(" ");
        emptyFields_exception();
    }

    @Test
    void saveVariable_NotEmptyFields_researchLayerId(){
        variableDTO.setIdCapaInvestigacion(" ");
        emptyFields_exception();
    }

    @Test
    void saveVariable_NotEmptyFields_type(){
        variableDTO.setTipo(" ");
        emptyFields_exception();
    }

    @Test
    void getVariableByResearchLayerId(){
        when(variableRepository.findAllByIdCapaInvestigacion(variableDTO.getIdCapaInvestigacion())).thenReturn(List.of(variableCollection));
        when(researchLayerRepository.existsById(variableDTO.getIdCapaInvestigacion())).thenReturn(true);
        when(variableMapper.toVariableDTO(variableCollection)).thenReturn(variableDTO);

        List<VariableDTO> result = variableService.getAllVariablesById(variableDTO.getIdCapaInvestigacion());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(variableDTO.getNombreVariable(), result.get(0).getNombreVariable());
        verify(variableRepository, times(1)).findAllByIdCapaInvestigacion(variableDTO.getIdCapaInvestigacion());
    }

    @Test
    void getVariableByVariableId(){
        when(variableRepository.findById(variableDTO.getId())).thenReturn(Optional.of(variableCollection));
        when(variableMapper.toVariableDTO(variableCollection)).thenReturn(variableDTO);

        VariableDTO result = variableService.getVariableById(variableDTO.getId());

        assertNotNull(result);
        assertEquals(variableDTO.getNombreVariable(), result.getNombreVariable());
        verify(variableRepository, times(1)).findById(variableDTO.getId());
    }

    @Test
    void getAllVariables(){
        when(variableRepository.findAll()).thenReturn(List.of(variableCollection));
        when(variableMapper.toVariableDTO(variableCollection)).thenReturn(variableDTO);

        List<VariableDTO> result = variableService.getAllVariables();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(variableDTO.getNombreVariable(), result.get(0).getNombreVariable());
        verify(variableRepository, times(1)).findAll();
    }

    private void emptyFields_exception(){
        NotEmptyFieldException exception = assertThrows(NotEmptyFieldException.class, () -> variableService.saveVariable(variableDTO));

        assertEquals("No se pueden ingresar valores vacios", exception.getMessage());
        verify(variableRepository, never()).save(variableCollection);
    }

    private void maxLengthEsceeded_exception(String message){
        when(researchLayerRepository.existsById(variableDTO.getIdCapaInvestigacion())).thenReturn(true);

        MaxLengthExceededException exception = assertThrows(MaxLengthExceededException.class, () -> variableService.saveVariable(variableDTO));

        assertEquals(message, exception.getMessage());
        verify(variableRepository, never()).save(variableCollection);
    }
}
