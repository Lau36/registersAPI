package com.example.registers_api.controllers;

import com.example.registers_api.dtos.LayerBossDTO;
import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.response.ResearchLayerResponse;
import com.example.registers_api.services.impl.ResearchLayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.registers_api.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ResearchLayerControllerTest {

    @InjectMocks
    private ResearchLayerController researchLayerController;

    @Mock
    private ResearchLayerService researchLayerService;

    private ResearchLayerDTO researchLayerDTO;
    private ResearchLayerResponse researchLayerResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        researchLayerDTO = new ResearchLayerDTO();
        researchLayerDTO.setLayerName("Capa 1");
        researchLayerDTO.setDescription("Descripción válida");
        researchLayerDTO.setLayerBoss(new LayerBossDTO(123, "Juan", "admin@gmail.com","ID123"));

        researchLayerResponse = new ResearchLayerResponse();
        researchLayerResponse.setId("layer123");
        researchLayerResponse.setLayerName("Capa 1");
        researchLayerResponse.setDescription("Descripción válida");
    }

    @Test
    void saveResearchLayer_Success() {
        // Arrange
        doNothing().when(researchLayerService).saveResearchLayer(any(ResearchLayerDTO.class));

        // Act
        ResponseEntity<BasicResponse> response = researchLayerController.saveLayer(researchLayerDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(RESEARCH_LAYER_CREATED, response.getBody().getMessage());
        verify(researchLayerService, times(1)).saveResearchLayer(any(ResearchLayerDTO.class));
    }

    @Test
    void getResearchLayerById_Success() {
        // Arrange
        when(researchLayerService.getResearchLayerById("layer123")).thenReturn(researchLayerResponse);

        // Act
        ResponseEntity<ResearchLayerResponse> response = researchLayerController.getResearchLayerById("layer123");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Capa 1", response.getBody().getLayerName());
        verify(researchLayerService, times(1)).getResearchLayerById("layer123");
    }

    @Test
    void getAllResearchLayers_Success() {
        // Arrange
        when(researchLayerService.getAllResearchLayers()).thenReturn(List.of(researchLayerResponse));

        // Act
        ResponseEntity<List<ResearchLayerResponse>> response = researchLayerController.getAllResearchLayers();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Capa 1", response.getBody().get(0).getLayerName());
        verify(researchLayerService, times(1)).getAllResearchLayers();
    }

    @Test
    void deleteResearchLayerById_Success() {
        // Arrange
        doNothing().when(researchLayerService).deleteResearchLayer("layer123");

        // Act
        ResponseEntity<BasicResponse> response = researchLayerController.deletResearchLayerById("layer123");

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RESEARCH_LAYER_DELETED, response.getBody().getMessage());
        verify(researchLayerService, times(1)).deleteResearchLayer("layer123");
    }

    @Test
    void updateResearchLayer_Success() {
        // Arrange
        doNothing().when(researchLayerService).updateResearchLayer(anyString(), any(ResearchLayerDTO.class));

        // Act
        ResponseEntity<BasicResponse> response = researchLayerController.updateLayer("layer123", researchLayerDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RESEARCH_LAYER_UPDATED, response.getBody().getMessage());
        verify(researchLayerService, times(1)).updateResearchLayer(anyString(), any(ResearchLayerDTO.class));
    }
}
