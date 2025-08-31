package com.example.registers_api.services;

import com.example.registers_api.dtos.LayerBossDTO;
import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.mappers.ResearchLayerMapper;
import com.example.registers_api.models.LayerBoss;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.response.ResearchLayerResponse;
import com.example.registers_api.services.impl.ResearchLayerService;
import com.example.registers_api.services.validations.ResearchLayerServiceValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResearchLayerServiceTest {
    @InjectMocks
    private ResearchLayerService researchLayerService;

    @Mock
    private ResearchLayerRepository researchLayerRepository;

    @Mock
    private ResearchLayerMapper researchLayerMapper;

    @Mock
    private ResearchLayerServiceValidations researchLayerServiceValidations;

    @Mock
    private RegisterRepository registerRepository;

    private ResearchLayerDTO sampleDTO;
    private ResearchLayerCollection sampleCollection;

    @BeforeEach
    void setUp() {
        LayerBossDTO layerBossDTO = new LayerBossDTO(1109660212, "Juan Pérez", "juan@gmail.com", "12345");
        sampleDTO = new ResearchLayerDTO();
        sampleDTO.setLayerName("Neurociencia");
        sampleDTO.setDescription("Investigación cerebral");
        sampleDTO.setLayerBoss(layerBossDTO);

        LayerBoss layerBoss = LayerBoss.builder()
                .id(1109660212)
                .name("Juan Pérez")
                .identificationNumber("12345")
                .build();

        sampleCollection = new ResearchLayerCollection();
        sampleCollection.setId("abc123");
        sampleCollection.setLayerName("Neurociencia");
        sampleCollection.setDescription("Investigación cerebral");
        sampleCollection.setLayerBoss(layerBoss);
        sampleCollection.setIsEnabled(true);
    }

    @Test
    void shouldSaveResearchLayerWhenNotExists() {
        // Arrange
        when(researchLayerRepository.findByLayerNameAndIsEnabled("Neurociencia", true))
                .thenReturn(Optional.empty());

        when(researchLayerMapper.toCollection(sampleDTO)).thenReturn(sampleCollection);

        // Act
        researchLayerService.saveResearchLayer(sampleDTO);

        // Assert
        verify(researchLayerRepository).save(any(ResearchLayerCollection.class));
    }

    @Test
    void shouldEnableDisabledLayerIfExists() {
        // Arrange
        sampleCollection.setIsEnabled(false);

        when(researchLayerRepository.findByLayerNameAndIsEnabled("Neurociencia", true))
                .thenReturn(Optional.of(sampleCollection));

        // Act
        researchLayerService.saveResearchLayer(sampleDTO);

        // Assert
        assertTrue(sampleCollection.getIsEnabled());
        verify(researchLayerRepository).save(sampleCollection);
    }

    @Test
    void shouldGetResearchLayerById() {
        when(researchLayerRepository.findByIdAndIsEnabled("abc123", true))
                .thenReturn(Optional.of(sampleCollection));

        ResearchLayerResponse response = new ResearchLayerResponse();
        when(researchLayerMapper.toResponse(sampleCollection)).thenReturn(response);

        ResearchLayerResponse result = researchLayerService.getResearchLayerById("abc123");

        assertNotNull(result);
    }

    @Test
    void shouldDeleteResearchLayerWhenRegisterExists() {
        when(registerRepository.existsByRegisterInfoResearchLayerId("abc123")).thenReturn(true);
        when(researchLayerRepository.findById("abc123")).thenReturn(Optional.of(sampleCollection));

        researchLayerService.deleteResearchLayer("abc123");

        verify(researchLayerRepository).save(sampleCollection);
        assertFalse(sampleCollection.getIsEnabled());
    }

    @Test
    void shouldCompletelyDeleteResearchLayerWhenNoRegisterExists() {
        when(registerRepository.existsByRegisterInfoResearchLayerId("abc123")).thenReturn(false);

        researchLayerService.deleteResearchLayer("abc123");

        verify(researchLayerRepository).deleteById("abc123");
    }

    @Test
    void shouldUpdateResearchLayerSuccessfully() {
        when(researchLayerRepository.findById("abc123")).thenReturn(Optional.of(sampleCollection));

        sampleDTO.setId("abc123");
        sampleDTO.setDescription("Nueva descripción");
        sampleDTO.setLayerName("Nueva capa");

        researchLayerService.updateResearchLayer("abc123", sampleDTO);

        verify(researchLayerRepository).save(any(ResearchLayerCollection.class));
    }

    @Test
    void getAllResearchLayers(){
        when(researchLayerRepository.findAllByIsEnabled(true)).thenReturn(List.of(sampleCollection));
        researchLayerService.getAllResearchLayers();
        verify(researchLayerRepository, times(1)).findAllByIsEnabled(true) ;
    }

    @Test
    void testDeleteResearchLayer_doesntExist_ThrowsException() {
        String researchLayerId = "researchLayerId2";

        when(registerRepository.existsByRegisterInfoResearchLayerId(researchLayerId)).thenReturn(true);

        when(researchLayerRepository.findById(researchLayerId)).thenReturn(Optional.empty());

        DoesntExistsException exception = assertThrows(DoesntExistsException.class, () ->
                researchLayerService.deleteResearchLayer(researchLayerId)
        );

        assertEquals("No se encontró una capa de investigación con el id: 'researchLayerId2'", exception.getMessage());

        verify(researchLayerRepository, times(1)).findById(researchLayerId);
    }
}
