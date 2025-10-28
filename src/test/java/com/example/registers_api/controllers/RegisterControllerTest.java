package com.example.registers_api.controllers;

import com.example.registers_api.controllers.RegisterController;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.response.RegisterResponse2;
import com.example.registers_api.response.ValidationResponse;
import com.example.registers_api.services.IRegisterService2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static com.example.registers_api.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    @Mock
    private IRegisterService2 registerService2;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- TEST: Save Register ---
    @Test
    void saveRegister_shouldReturnSuccessResponse() {
        RegisterRequest request = new RegisterRequest();
        String userEmail = "doctor@example.com";

        ResponseEntity<BasicResponse> response = registerController.saveRegister(userEmail, request);

        verify(registerService2).saveRegister(request, userEmail);
        assertEquals(REGISTER_CREATED_SUCCESSFULL, response.getBody().getMessage());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Update Register ---
    @Test
    void updateRegister_shouldReturnUpdatedResponse() {
        String registerId = "reg123";
        String userEmail = "doctor@example.com";
        RegisterRequest request = new RegisterRequest();

        ResponseEntity<BasicResponse> response =
                registerController.updateRegister(registerId, userEmail, request);

        verify(registerService2).updateRegister(registerId, request, userEmail);
        assertEquals(REGISTER_UPDATED, response.getBody().getMessage());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Delete Register ---
    @Test
    void deleteRegister_shouldReturnDeletedResponse() {
        String registerId = "reg123";

        ResponseEntity<BasicResponse> response = registerController.deleteRegisterById(registerId);

        verify(registerService2).deleteRegister(registerId);
        assertEquals(REGISTER_DELETED, response.getBody().getMessage());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Get All Research Layer Registers ---
    @Test
    void getAllRegistersByResearchLayer_shouldReturnPaginatedResponse() {
        PaginatedResponse paginatedResponse = new PaginatedResponse();
        when(registerService2.getAllRegistersByResearchLayerPaginated(any(), any(), any(), any()))
                .thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponse> response = registerController.getAllRegistersByResearchLayer(
                "layer1", "user@example.com", 123, 0, 10, "date", "ASC"
        );

        verify(registerService2).getAllRegistersByResearchLayerPaginated(any(), eq("layer1"), eq("user@example.com"), eq(123));
        assertEquals(paginatedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Get All Caregiver Registers ---
    @Test
    void getAllCaregiverRegisters_shouldReturnPaginatedResponse() {
        PaginatedResponse paginatedResponse = new PaginatedResponse();
        when(registerService2.getAllCaregiverRegistersPaginated(any(), anyInt()))
                .thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponse> response = registerController.getAllCaregiverRegisters(
                123, 0, 10, "date", "ASC"
        );

        verify(registerService2).getAllCaregiverRegistersPaginated(any(), eq(123));
        assertEquals(paginatedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Get All Patient Basic Info Registers ---
    @Test
    void getAllPatientBasicInfoRegisters_shouldReturnPaginatedResponse() {
        PaginatedResponse paginatedResponse = new PaginatedResponse();
        when(registerService2.getAllPatientBasicInfoRegistersPaginated(any(), anyInt()))
                .thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponse> response = registerController.getAllPatientBasicInfoRegisters(
                123, 0, 10, "date", "ASC"
        );

        verify(registerService2).getAllPatientBasicInfoRegistersPaginated(any(), eq(123));
        assertEquals(paginatedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Get Register by Patient ---
    @Test
    void getRegisterByPatient_shouldReturnRegisterResponse() {
        RegisterResponse2 registerResponse = new RegisterResponse2();
        when(registerService2.actualPatientRegisterInfo(anyInt(), anyString()))
                .thenReturn(registerResponse);

        ResponseEntity<RegisterResponse2> response = registerController.getRegisterByPatient(123, "layer1");

        verify(registerService2).actualPatientRegisterInfo(123, "layer1");
        assertEquals(registerResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Get Research Layer History ---
    @Test
    void getAllResearchLayerHistoryById_shouldReturnPaginatedResponse() {
        PaginatedResponse paginatedResponse = new PaginatedResponse();
        when(registerService2.getAllRegisterInfoByResearchLayerPaginated(any(), anyString(), anyString()))
                .thenReturn(paginatedResponse);

        ResponseEntity<PaginatedResponse> response = registerController.getAllResearchLayerHistoryById(
                "layer1", "user@example.com", 0, 10, "date", "DESC"
        );

        verify(registerService2).getAllRegisterInfoByResearchLayerPaginated(any(), eq("layer1"), eq("user@example.com"));
        assertEquals(paginatedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    // --- TEST: Validate Patient ---
    @Test
    void validateUserGet_shouldReturnValidationResponse() {
        ValidationResponse validationResponse = new ValidationResponse();
        when(registerService2.validateUserAndGetCurrent(anyString(), anyString(), anyInt()))
                .thenReturn(validationResponse);

        ResponseEntity<ValidationResponse> response = registerController.validateUserGet(
                "layer1", "user@example.com", 123
        );

        verify(registerService2).validateUserAndGetCurrent("user@example.com", "layer1", 123);
        assertEquals(validationResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}

