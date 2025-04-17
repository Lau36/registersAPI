package com.example.registers_api.services;

import com.example.registers_api.models.*;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.RegistersResponse;
import com.example.registers_api.services.impl.RegisterService;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.InjectMocks;
import org.Mock;
import org.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ArgumentMatchers.any;
import static org.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoAnnotations.class)
class RegisterServiceTest {
    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private RegistersServiceValidations registerServiceValidations;

    @InjectMocks
    private RegisterService registerService;

    private RegisterRequest request;
    private RegisterCollection entity;
    private RegistersResponse response;

    @BeforeEach
    void setUp() {
        request = RegisterRequest.builder()
                .variables(List.of(new Variable("v1", "value", "String", "rl1")))
                .patientIdentificationNumber(123)
                .patientIdentificationType("CC")
                .patient(Patient.builder()
                        .name("Juan")
                        .sex("M")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .age(33)
                        .email("juan@mail.com")
                        .phoneNumber("1234567890")
                        .economicStatus("Medio")
                        .educationLevel("Universitario")
                        .maritalStatus("Soltero")
                        .hometown("CiudadA")
                        .currentCity("CiudadB")
                        .firstCrisisDate("2020-01-01")
                        .crisisStatus("Activo")
                        .build())
                .caregiver(new Caregiver("Maria", "CC", 111, 50, "Bachiller", "Ama de casa"))
                .healthProfessional(new HealthProfessional("hp1", "Dr. López", 999))
                .build();

        entity = RegisterCollection.builder()
                .id("reg123")
                .variables(request.getVariables())
                .patientIdentificationNumber(request.getPatientIdentificationNumber())
                .patientIdentificationType(request.getPatientIdentificationType())
                .patientBasicInfo(request.getPatient())
                .caregiver(request.getCaregiver())
                .healthProfessional(request.getHealthProfessional())
                .build();

        response = RegistersResponse.builder()
                .patientIdentificationNumber(123)
                .patientIdentificationType("CC")
                .build();
    }

    @Test
    void shouldCreateRegister() {
        when(registerRepository.save(entity)).thenReturn(entity);

        registerService.saveRegister(request, "email@mgail.com");

        verify(registerServiceValidations, times(1)).validateRegisterFields(request);
        verify(registerRepository, times(1)).save(entity);
    }

//    @Test
//    void shouldFindById() {
//        when(registerRepository.findById("reg123")).thenReturn(Optional.of(entity));
//
//        RegistersResponse result = registerService.("reg123");
//
//        assertEquals(response, result);
//        verify(registerRepository).findById("reg123");
//        verify(registerMapper).toResponse(entity);
//    }

//    @Test
//    void findById_ShouldThrowIfNotFound() {
//        when(registerRepository.findById("not-found")).thenReturn(Optional.empty());
//
//        Assertions.assertThrows(NotFoundException.class, () -> {
//            registerService.findById("not-found");
//        });
//    }

    @Test
    void shouldUpdateRegister() {
        // Arrange

        HealthProfessional newDoctor = new HealthProfessional("id ", "Dr. Lucas", 1109660245)
        RegisterCollection newRegister = new RegisterCollection();
        request.setHealthProfessional(newDoctor);
        newRegister.setHealthProfessional(newDoctor);

        when(registerRepository.findById("reg123")).thenReturn(Optional.of(entity));
        when(registerRepository.save(newRegister)).thenReturn(newRegister);

        // Act
        registerService.updateRegister("reg123", "user1gmail.com", request);

        // Assert
//        verify(validations).validateUpdateRequest("reg123", newRequest);
        verify(registerRepository, times(1)).save(newRegister);
    }

    @Test
    void shouldDeleteRegister() {
        when(registerRepository.existsById("reg123")).thenReturn(true);

        registerService.deleteRegister("reg123");

        verify(registerRepository).deleteById("reg123");
    }

    @Test
    void delete_ShouldThrowIfNotFound() {
        when(registerRepository.existsById("Register id")).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            registerService.deleteRegister("Register id");
        });
    }
}
