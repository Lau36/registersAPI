package com.example.registers_api.controllers;

import com.example.registers_api.dtos.LayerBossDTO;
import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.ResearchLayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        researchLayerDTO = new ResearchLayerDTO();
        researchLayerDTO.setNombreCapa("Capa 1");
        researchLayerDTO.setDescripcion("Descripción válida");
        researchLayerDTO.setJefeCapa(new LayerBossDTO(123, "Juan", "ID123"));

    }

    @Test
    void saveResearchLayer() {
        doNothing().when(researchLayerService).saveResearchLayer(researchLayerDTO);

        ResponseEntity<BasicResponse> response = researchLayerController.saveLayer(researchLayerDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Se creó la capa de investigación correctamente", response.getBody().getMessage());
        verify(researchLayerService, times(1)).saveResearchLayer(any(ResearchLayerDTO.class));
    }

    @Test
    void getResearchLayerById() {
        when(researchLayerService.getResearchLayerById(researchLayerDTO.getId())).thenReturn(researchLayerDTO);

        ResponseEntity<ResearchLayerDTO> response = researchLayerController.getResearchLayerById(researchLayerDTO.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(researchLayerDTO, response.getBody());
    }

    @Test
    void getAllResearchLayers() {
        when(researchLayerService.getAllResearchLayers()).thenReturn(List.of(researchLayerDTO));

        ResponseEntity<List<ResearchLayerDTO>> response = researchLayerController.getAllResearchLayers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(researchLayerDTO, response.getBody().get(0));
    }
}
