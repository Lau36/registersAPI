package com.example.registers_api.controllers;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.response.VariablesResponse;
import com.example.registers_api.services.IVariableService;
import com.example.registers_api.services.impl.VariableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.example.registers_api.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VariableControllerTest {

    @InjectMocks
    private VariablesController variablesController;

    @Mock
    private IVariableService variableService;

    private VariableDTO variableDTO;
    private VariablesResponse variablesResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        variableDTO = new VariableDTO();
        variableDTO.setVariableName("Frecuencia cardíaca");
        variableDTO.setDescription("Medición del ritmo cardíaco por minuto");

        variablesResponse = new VariablesResponse();
        variablesResponse.setId("var001");
        variablesResponse.setVariableName("Frecuencia cardíaca");
        variablesResponse.setDescription("Medición del ritmo cardíaco por minuto");
    }

    @Test
    void saveVariable_Success() {
        doNothing().when(variableService).saveVariable(any(VariableDTO.class));

        ResponseEntity<BasicResponse> response = variablesController.saveVariable(variableDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(VARIABLE_CREATED, response.getBody().getMessage());
        verify(variableService, times(1)).saveVariable(any(VariableDTO.class));
    }

    @Test
    void updateVariable_Success() {
        doNothing().when(variableService).updateVariable(anyString(), any(VariableDTO.class));

        ResponseEntity<BasicResponse> response = variablesController.updateVariable("var001", variableDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VARIABLE_UPDATED, response.getBody().getMessage());
        verify(variableService, times(1)).updateVariable(eq("var001"), any(VariableDTO.class));
    }

    @Test
    void deleteVariable_Success() {
        doNothing().when(variableService).deleteVariable("var001");

        ResponseEntity<BasicResponse> response = variablesController.deleteVariable("var001");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(VARIABLE_DELETED, response.getBody().getMessage());
        verify(variableService, times(1)).deleteVariable("var001");
    }

    @Test
    void getVariablesByResearchLayerId_Success() {
        when(variableService.getAllVariablesById("layer123")).thenReturn(List.of(variablesResponse));

        ResponseEntity<List<VariablesResponse>> response =
                variablesController.getVariablesByResearchLayerId("layer123");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Frecuencia cardíaca", response.getBody().get(0).getVariableName());
        verify(variableService, times(1)).getAllVariablesById("layer123");
    }

    @Test
    void getVariableById_Success() {
        when(variableService.getVariableById("var001")).thenReturn(variablesResponse);

        ResponseEntity<VariablesResponse> response = variablesController.getVariableById("var001");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Frecuencia cardíaca", response.getBody().getVariableName());
        verify(variableService, times(1)).getVariableById("var001");
    }

    @Test
    void getAllVariables_Success() {
        when(variableService.getAllVariables()).thenReturn(List.of(variablesResponse));

        ResponseEntity<List<VariablesResponse>> response = variablesController.getAllVariables();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Frecuencia cardíaca", response.getBody().get(0).getVariableName());
        verify(variableService, times(1)).getAllVariables();
    }
}
