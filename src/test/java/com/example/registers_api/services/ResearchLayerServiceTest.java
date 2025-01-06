package com.example.registers_api.services;

import com.example.registers_api.dtos.LayerBossDTO;
import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.LayerBoss;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResearchLayerServiceTest {
    @Mock
    private ResearchLayerRepository researchLayerRepository;

    @Mock
    private ResearchLayerMapper researchLayerMapper;

    @InjectMocks
    private ResearchLayerService researchLayerService;

    private ResearchLayerDTO researchLayerDTO;
    private ResearchLayerCollection researchLayerCollection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        researchLayerDTO = new ResearchLayerDTO();
        researchLayerDTO.setNombreCapa("Capa 1");
        researchLayerDTO.setDescripcion("Descripción válida");
        researchLayerDTO.setJefeCapa(new LayerBossDTO(123, "Juan", "ID123"));

        LayerBoss layerBoss = new LayerBoss(123, "Juan", "ID123");
        researchLayerCollection = new ResearchLayerCollection("Capa 1", "Descripcion válida", layerBoss);
    }

    @Test
    void saveResearchLayer_ValidInput_Success() {

        when(researchLayerMapper.toCollection(researchLayerDTO)).thenReturn(researchLayerCollection);
        when(researchLayerRepository.existsByNombreCapa("Capa 1")).thenReturn(false);

        assertDoesNotThrow(() -> researchLayerService.saveResearchLayer(researchLayerDTO));

        verify(researchLayerRepository, times(1)).save(researchLayerCollection);
    }

    @Test
    void saveResearchLayer_AlreadyExists_ThrowsException() {
        when(researchLayerRepository.existsByNombreCapa("Capa 1")).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> researchLayerService.saveResearchLayer(researchLayerDTO));

        assertEquals("Ya existe una capa de investigación con el nombre 'Capa 1'", exception.getMessage());
        verify(researchLayerRepository, never()).save(any());
    }

    @Test
    void saveResearchLayer_TooLongFields_ThrowsException() {
        researchLayerDTO.setDescripcion("x".repeat(201));

        MaxLengthExceededException exception = assertThrows(MaxLengthExceededException.class,
                () -> researchLayerService.saveResearchLayer(researchLayerDTO));

        assertEquals("El campo descripcion no puede exceder los 200 caracteres", exception.getMessage());
    }

    @Test
    void getResearchLayerById_ValidId_ReturnsDTO() {

        when(researchLayerRepository.findById("1")).thenReturn(Optional.of(researchLayerCollection));
        when(researchLayerMapper.toDto(researchLayerCollection)).thenReturn(researchLayerDTO);

        ResearchLayerDTO result = researchLayerService.getResearchLayerById("1");

        assertNotNull(result);
        assertEquals("Capa 1", result.getNombreCapa());
    }

    @Test
    void getResearchLayerById_InvalidId_ThrowsException() {
        when(researchLayerRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> researchLayerService.getResearchLayerById("1"));
    }

    @Test
    void getAllResearchLayers_ReturnsList() {

        when(researchLayerRepository.findAll()).thenReturn(List.of(researchLayerCollection));
        when(researchLayerMapper.toDto(researchLayerCollection)).thenReturn(researchLayerDTO);


        List<ResearchLayerDTO> result = researchLayerService.getAllResearchLayers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Capa 1", result.get(0).getNombreCapa());
    }
}
