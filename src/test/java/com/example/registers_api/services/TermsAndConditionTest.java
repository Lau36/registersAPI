package com.example.registers_api.services;

import com.example.registers_api.models.TermsAndConditionsCollection;
import com.example.registers_api.repository.TermsConditionsRepository;
import com.example.registers_api.response.TermsConditionsResponse;
import com.example.registers_api.services.impl.TermsAndConditionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TermsAndConditionTest {
    @Mock
    private TermsConditionsRepository termsConditionsRepository;

    @InjectMocks
    private TermsAndConditionsService termsAndConditionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTermsAndConditions_ShouldReturnResponse_WhenCollectionExists() {
        TermsAndConditionsCollection collection = new TermsAndConditionsCollection();
        collection.setTermsConditions("Estos son los términos y condiciones...");

        when(termsConditionsRepository.findAll()).thenReturn(List.of(collection));

        TermsConditionsResponse response = termsAndConditionsService.getTermsAndConditions();

        assertNotNull(response);
        assertEquals("Estos son los términos y condiciones...", response.getTermsAndConditionsInfo());
        verify(termsConditionsRepository).findAll();
    }

    @Test
    void getTermsAndConditions_ShouldThrowException_WhenNoCollectionsFound() {
        when(termsConditionsRepository.findAll()).thenReturn(List.of());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            termsAndConditionsService.getTermsAndConditions();
        });

        verify(termsConditionsRepository ).findAll();
    }
}
