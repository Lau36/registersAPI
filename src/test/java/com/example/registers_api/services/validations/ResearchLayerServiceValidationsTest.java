package com.example.registers_api.services.validations;

import com.example.registers_api.dtos.LayerBossDTO;
import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.AlreadyExistsException;
import com.example.registers_api.exceptions.MaxLengthExceededException;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.models.LayerBoss;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResearchLayerServiceValidationsTest {
    @Mock
    private ResearchLayerRepository researchLayerRepository;

    @InjectMocks
    private ResearchLayerServiceValidations validations;

    private ResearchLayerDTO dto;
    private LayerBossDTO boss;
    private LayerBoss boss2;
    private ResearchLayerCollection layerCollection;
    
    @BeforeEach
    void setUp() {
        boss = new LayerBossDTO();
        boss.setId(1);
        boss.setName("Juan Pérez");
        boss.setIdentificationNumber("123456");

        boss2 = new LayerBoss();
        boss2.setId(1109880212);
        boss2.setName("Laura Jaimes");
        boss2.setIdentificationNumber("123456789");

        layerCollection = new ResearchLayerCollection();
        layerCollection.setId("layer123");
        layerCollection.setLayerName("Capa de Neurociencia");
        layerCollection.setDescription("Estudios sobre epilepsia y neuroimagen");
        layerCollection.setLayerBoss(boss2);
        layerCollection.setIsEnabled(true);
        layerCollection.setCreatedAt(LocalDateTime.now());
        layerCollection.setUpdatedAt(LocalDateTime.now());

        dto = new ResearchLayerDTO();
        dto.setLayerName("Nombre de capa");
        dto.setDescription("Descripción");
        dto.setLayerBoss(boss);
    }

    @Test
    void notEmptyValidations_shouldThrowException_whenFieldsAreEmpty() {
        dto.setLayerName(" ");
        assertThrows(NotEmptyFieldException.class, () -> validations.notEmptyValidations(dto));

        dto.setLayerName("Nombre");
        dto.setDescription(" ");
        assertThrows(NotEmptyFieldException.class, () -> validations.notEmptyValidations(dto));

        dto.setDescription("Descripción");
        boss.setName(" ");
        assertThrows(NotEmptyFieldException.class, () -> validations.notEmptyValidations(dto));

        boss.setName("Nombre");
        boss.setId(0);
        assertThrows(NotEmptyFieldException.class, () -> validations.notEmptyValidations(dto));

        boss.setId(1);
        boss.setIdentificationNumber(" ");
        assertThrows(NotEmptyFieldException.class, () -> validations.notEmptyValidations(dto));
    }

    @Test
    void alreadyExistsResearchLayerValidation_shouldThrowException_whenLayerNameAlreadyExists() {
        when(researchLayerRepository.existsByLayerNameAndIsEnabled(dto.getLayerName(), true)).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> validations.alreadyExistsResearchLayerValidation(dto));
    }

    @Test
    void alreadyExistsResearchLayerValidationUpdate_shouldThrowException_whenDifferentLayerExistsWithSameName() {
        ResearchLayerCollection existing = new ResearchLayerCollection();
        existing.setId("123");

        ResearchLayerCollection other = new ResearchLayerCollection();
        other.setId("999");

        when(researchLayerRepository.findByLayerNameAndIsEnabled(dto.getLayerName(), true))
                .thenReturn(Optional.of(other));

        assertThrows(AlreadyExistsException.class, () -> validations.alreadyExistsResearchLayerValidationUpdate(dto, existing));
    }

    @Test
    void alreadyExistsResearchLayerValidationUpdate_shouldNotThrow_whenSameLayerIsBeingUpdated() {
        ResearchLayerCollection existing = new ResearchLayerCollection();
        existing.setId("123");

        when(researchLayerRepository.findByLayerNameAndIsEnabled(dto.getLayerName(), true))
                .thenReturn(Optional.of(existing));

        assertDoesNotThrow(() -> validations.alreadyExistsResearchLayerValidationUpdate(dto, existing));
    }

    @Test
    void tooLongValidations_shouldThrowException_whenNameIsTooLong() {
        dto.setLayerName("a".repeat(101));
        assertThrows(MaxLengthExceededException.class, () -> validations.tooLongValidations(dto));
    }

    @Test
    void tooLongValidations_shouldThrowException_whenDescriptionIsTooLong() {
        dto.setDescription("a".repeat(201));
        assertThrows(MaxLengthExceededException.class, () -> validations.tooLongValidations(dto));
    }

    @Test
    void tooLongValidations_shouldNotThrow_whenLengthsAreValid() {
        dto.setLayerName("a".repeat(100));
        dto.setDescription("a".repeat(200));
        assertDoesNotThrow(() -> validations.tooLongValidations(dto));
    }
}
