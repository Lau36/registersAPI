package com.example.registers_api.controllers;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.VariableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VariableControllerTest {
    @InjectMocks
    private VariablesController variableController;

    @Mock
    private VariableService variableService;

    private VariableDTO variableDTO;
    private List<VariableDTO> variableDTOList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        variableDTO = new VariableDTO();
        variableDTO.setId("1");
        variableDTO.setNombreVariable("Variable Test");

        variableDTOList = new ArrayList<>();
        variableDTOList.add(variableDTO);
    }

    @Test
    void testSaveVariable() {
        doNothing().when(variableService).saveVariable(any(VariableDTO.class));

        ResponseEntity<BasicResponse> response = variableController.saveVariable(variableDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Se creó la variable correctamente", response.getBody().getMessage());
        verify(variableService, times(1)).saveVariable(any(VariableDTO.class));
    }

    @Test
    void testGetVariablesByResearchLayerId() {
        when(variableService.getAllVariablesById("123")).thenReturn(variableDTOList);

        ResponseEntity<List<VariableDTO>> response = variableController.getVariablesByResearchLayerId("123");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Variable Test", response.getBody().get(0).getNombreVariable());
        verify(variableService, times(1)).getAllVariablesById("123");
    }

    @Test
    void testGetVariableById() {
        when(variableService.getVariableById("1")).thenReturn(variableDTO);

        ResponseEntity<VariableDTO> response = variableController.getVariableById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Variable Test", response.getBody().getNombreVariable());
        verify(variableService, times(1)).getVariableById("1");
    }

    @Test
    void testGetAllResearchLayers() {
        when(variableService.getAllVariables()).thenReturn(variableDTOList);

        ResponseEntity<List<VariableDTO>> response = variableController.getAllResearchLayers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Variable Test", response.getBody().get(0).getNombreVariable());
        verify(variableService, times(1)).getAllVariables();
    }
}
