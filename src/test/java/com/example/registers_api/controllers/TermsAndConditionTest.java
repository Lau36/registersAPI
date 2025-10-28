package com.example.registers_api.controllers;

import com.example.registers_api.response.TermsConditionsResponse;
import com.example.registers_api.services.ITermsAndConditions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TermsAndConditionsControllerTest {

    @Mock
    private ITermsAndConditions termsAndConditions;

    @InjectMocks
    private TermsAndConditionsController termsAndConditionsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllResearchLayers_shouldReturnResponseSuccessfully() {
        TermsConditionsResponse mockResponse = new TermsConditionsResponse();
        when(termsAndConditions.getTermsAndConditions()).thenReturn(mockResponse);

        ResponseEntity<TermsConditionsResponse> response = termsAndConditionsController.getAllResearchLayers();

        verify(termsAndConditions).getTermsAndConditions();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
    }
}
